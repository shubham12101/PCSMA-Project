import urllib2
from django.http import HttpResponse
from django.shortcuts import render
from rest_framework import status,viewsets
from rest_framework.views import APIView
from rest_framework.response import Response
from rest_framework.authtoken.models import Token
from rest_framework.authtoken.serializers import AuthTokenSerializer
from rest_framework.parsers import JSONParser, MultiPartParser, FormParser
from rest_framework.renderers import JSONRenderer
from main.serializers import UserProfileSerializer, MenuSerializer, CourseSerializer
from main.models import UserProfile, Menu, Course
from main.functions import get_time_slot,get_day
from gcm.models import get_device_model
from social.apps.django_app.utils import psa

# Create your views here.
class ObtainAuthToken(APIView):
    throttle_classes = ()
    permission_classes = ()
    authentication_classes = ()
    parser_classes = (FormParser, MultiPartParser, JSONParser,)
    renderer_classes = (JSONRenderer,)
    serializer_class = AuthTokenSerializer
    model = Token

    # Accept backend as a parameter and 'auth' for a login / pass
    def post(self, request, backend):
        if backend == 'auth':
            serializer = self.serializer_class(data=request.DATA)
            if serializer.is_valid():
                token, created = Token.objects.get_or_create(user=serializer.object['user'])
                return Response({'token': token.key})
            return Response(serializer.errors, status=status.HTTP_400_BAD_REQUEST)
        else:
            # Here we call PSA to authenticate like we would if we used PSA on server side.
            # print 'calling PSA'
            user = register_by_access_token(request, backend)
            # If user is active we get or create the REST token and send it back with user data
            if user and user.is_active:
                token, created = Token.objects.get_or_create(user=user)
                return Response({'id': user.id, 'name': user.username, 'userRole': 'user', 'token': token.key})


@psa(redirect_uri=None)
def register_by_access_token(request, backend):
    # Split by spaces and get the array
    auth = get_authorization_header(request).split()

    if not auth or auth[0].lower() != b'token':
        msg = 'No token header provided.'
        return msg
    
    if len(auth) == 1:
        msg = 'Invalid token header. No credentials provided.'
        return msg
    
    access_token = auth[1]
    
    # Real authentication takes place here
    user = request.backend.do_auth(access_token)
    return user

def get_authorization_header(request):
    return request.META['HTTP_AUTHORIZATION']


class UserProfileViewSet(APIView):
    queryset = UserProfile.objects.all()
    serializer_class = UserProfileSerializer

    def get(self, request, format=None):
        serializer = UserProfileSerializer(self.queryset, many=True)
        return Response(serializer.data)

class MenuViewSet(APIView):
    queryset = Menu.objects.all()
    serializer_class = MenuSerializer

    def get(self, request, format=None):
    	time_string=request.GET.get('time','')
    	print time_string
    	if(time_string==''):
        	serializer = MenuSerializer(self.queryset, many=True)
    	else:
    		time=float(time_string)
    		time=time/1000
    		day_of_week= get_day(time)
    		# print day_of_week 
    		slot= get_time_slot(time)
    		# print slot
    		if(slot!=""):
    			serializer = MenuSerializer(Menu.objects.get(day=day_of_week,time_slot=slot))
    		else:
    			return HttpResponse(status=404)
        return Response(serializer.data)

class CourseViewSet(APIView):
    queryset = Course.objects.all()
    serializer_class = CourseSerializer

    def get(self, request, format=None):
    	# course_name=request.data.get('course_name','')
    	course_name=request.GET.get('course_name','')
    	# print request.GET.get('sem')
    	# print course_name
    	# print request.GET['course_name']
    	if(course_name==''):
        	serializer = CourseSerializer(self.queryset, many=True)
    	else:
    		serializer=CourseSerializer(Course.objects.get(name=course_name))
       	return Response(serializer.data)

class MessageViewSet(APIView):

    def post(self, request, format=None):
        Device = get_device_model()
        rcvr_device = Device.objects.get(name='vedant12118@iiitd.ac.in')
        rcvr_device.send_message('django test message',collapse_key='inform')
        return HttpResponse(status=200)

class LibraryViewSet(APIView):
    
    def get(self, request, format=None):
        library_url="http://192.168.48.103:8080/"
        book_api="books/"

        issued_by=request.GET.get('issued_by','')
        if(issued_by==''):
            return HttpResponse(status=400)
        else:
            url_string=library_url+book_api+"?issued_by="+issued_by
            data=urllib2.urlopen(url_string).read()
            print data
            return Response(serializer.data)

              

