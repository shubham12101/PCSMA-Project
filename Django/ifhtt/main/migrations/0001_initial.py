# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
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
                ('day', models.CharField(unique=True, max_length=200)),
                ('items', models.CharField(max_length=200)),
                ('time_slot', models.CharField(max_length=200)),
            ],
            options={
                'db_table': 'menu',
            },
            bases=(models.Model,),
        ),
        migrations.CreateModel(
            name='UserProfile',
            fields=[
                ('id', models.AutoField(verbose_name='ID', serialize=False, auto_created=True, primary_key=True)),
                ('username', models.CharField(max_length=200)),
                ('gcm_id', models.CharField(unique=True, max_length=200)),
            ],
            options={
                'db_table': 'user_profile',
            },
            bases=(models.Model,),
        ),
    ]
