# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('main', '0003_remove_userprofile_gcm_id'),
    ]

    operations = [
        migrations.RemoveField(
            model_name='userprofile',
            name='auth_user',
        ),
        migrations.DeleteModel(
            name='UserProfile',
        ),
    ]
