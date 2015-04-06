# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('main', '0003_auto_20150328_0008'),
    ]

    operations = [
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
    ]
