#!/bin/bash
git clone https://github.com/kaleidos-ventures/taiga-docker.git
cd taiga-docker
git checkout stable
sudo ./launch-all.sh
sudo ./taiga-manage.sh createsuperuser

