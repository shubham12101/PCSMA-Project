from main.models import Menu, Course
from rest_framework import serializers
from django.contrib.auth.models import User

class UserSerializer(serializers.HyperlinkedModelSerializer):
	class Meta:
		model = User
		fields = ('email','first_name','last_name')

class MenuSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
    	model = Menu
        fields = ('day','items','time_slot')

class CourseSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
    	model = Course
        fields =('course_id','name','website')