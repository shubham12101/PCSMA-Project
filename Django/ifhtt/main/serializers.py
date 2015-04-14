from main.models import Menu, Course
from rest_framework import serializers

class MenuSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
    	model = Menu
        fields = ('day','items','time_slot')

class CourseSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
    	model = Course
        fields =('course_id','name','website')