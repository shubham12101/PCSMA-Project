from django.http import HttpResponse
from django.shortcuts import render
from rest_framework import status,viewsets
from rest_framework.views import APIView
from rest_framework.response import Response
from main.serializers import UserProfileSerializer, MenuSerializer, CourseSerializer
from main.models import UserProfile, Menu, Course
from main.functions import get_time_slot,get_day

# Create your views here.
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

