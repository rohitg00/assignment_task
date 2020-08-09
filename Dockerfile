FROM centos

RUN yum install httpd -y
RUN systemctl enable httpd
RUN yum install virtualenv -y
RUN yum install python36 python3-virtualenv -y
RUN virtualenv django
RUN source django/bin/activate
RUN pip3 --no-cache-dir install numpy
RUN yum install python3-pip -y
RUN yum install python3-setuptools -y
RUN yum install python3-wheel -y
RUN yum install pkg-config -y
RUN yum install git -y
RUN yum install sudo -y
RUN pip3 --no-cache-dir install
RUN pip3 install --upgrade setuptools
RUN pip3 install ez_setup
RUN pip3 install --upgrade pip
RUN pip3 install        django==3.1 \
                        gunicorn \
                        bpython==0.12 \
                        django-braces==0.2.1 \
                        django-model-utils==1.1.0 \
                        logutils==0.3.3 \
                        South==0.7.6 \
                        requests==1.2.0 \
                        dj-database-url==0.2.1 \
                        django-oauth2-provider==0.2.4 \
                        djangorestframework==3.3.1
