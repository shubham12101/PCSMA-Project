from django.shortcuts import render
from rest_framework import status,viewsets
from rest_framework.views import APIView
from rest_framework.response import Response
from main.serializers import UserProfileSerializer, MenuSerializer, CourseSerializer
from main.models import UserProfile, Menu, Course

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
        serializer = MenuSerializer(self.queryset, many=True)
        return Response(serializer.data)

class CourseViewSet(APIView):
    queryset = Course.objects.all()
    serializer_class = CourseSerializer

    def get(self, request, format=None):
        serializer = CourseSerializer(self.queryset, many=True)
        return Response(serializer.data)



