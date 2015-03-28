from django.conf.urls import patterns, include, url
from django.contrib import admin
from main import views

urlpatterns = patterns('',
    # Examples:
    # url(r'^$', 'ifhtt.views.home', name='home'),
    # url(r'^blog/', include('blog.urls')),

    url(r'^admin/', include(admin.site.urls)),
    url(r'^user-profiles/', views.UserProfileViewSet.as_view()),
    url(r'^menu/$', views.MenuViewSet.as_view()),
    url(r'^courses/$', views.CourseViewSet.as_view()),
)
