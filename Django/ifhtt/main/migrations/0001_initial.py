# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations
from django.conf import settings


class Migration(migrations.Migration):

    dependencies = [
        migrations.swappable_dependency(settings.AUTH_USER_MODEL),
    ]

    operations = [
        migrations.CreateModel(
            name='Course',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('course_id', models.CharField(unique=True, max_length=200)),
                ('name', models.CharField(max_length=200)),
                ('website', models.CharField(max_length=200)),
            ],
            options={
                'db_table': 'course',
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='Menu',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('day', models.CharField(max_length=200)),
                ('items', models.CharField(max_length=200)),
                ('time_slot', models.CharField(max_length=200)),
            ],
            options={
                'db_table': 'menu',
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='UserDevice',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('dev_id', models.CharField(unique=True, max_length=50, verbose_name='Device ID')),
                ('reg_id', models.CharField(unique=True, max_length=255, verbose_name='Registration ID')),
                ('name', models.CharField(max_length=255, null=True, verbose_name='Name', blank=True)),
                ('creation_date', models.DateTimeField(auto_now_add=True, verbose_name='Creation date')),
                ('modified_date', models.DateTimeField(auto_now=True, verbose_name='Modified date')),
                ('is_active', models.BooleanField(default=False, verbose_name='Is active?')),
            ],
            options={
                'ordering': ['-modified_date'],
                'abstract': False,
                'verbose_name': 'Device',
                'verbose_name_plural': 'Devices',
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='UserProfile',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('username', models.CharField(max_length=200)),
                ('gcm_id', models.CharField(unique=True, max_length=200)),
                ('auth_user', models.ForeignKey(to=settings.AUTH_USER_MODEL, null=True)),
            ],
            options={
                'db_table': 'user_profile',
            },
            bases=(models.Model,),
        ),
    ]
