# -*- coding: utf-8 -*-
from __future__ import unicode_literals

from django.db import models, migrations


class Migration(migrations.Migration):

    dependencies = [
        ('registry', '0001_initial'),
    ]

    operations = [
        migrations.AlterField(
            model_name='book',
            name='issued_by',
            field=models.EmailField(max_length=75, null=True),
            preserve_default=True,
        ),
    ]
