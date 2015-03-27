from main.models import UserProfile,Menu, Course
from rest_framework import serializers

class UserProfileSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
    	model = UserProfile
        fields =('gcm_id','username')

class MenuSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
    	model = Menu
        fields = ('day','items','time_slot')

class CourseSerializer(serializers.HyperlinkedModelSerializer):
    class Meta:
    	model = Course
        fields =('course_id','name','website')