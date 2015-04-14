from django.contrib import admin
from main.models import Menu,Course

class MenuAdmin(admin.ModelAdmin):
	list_display=('day','items','time_slot')

class CourseAdmin(admin.ModelAdmin):
	list_display=('course_id','name','website')	

# Register your models here.
admin.site.register(Menu,MenuAdmin)
admin.site.register(Course,CourseAdmin)
