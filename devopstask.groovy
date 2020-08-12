job("task_job1_gitpull") {
description ("It will pull code from GitHub")
  scm{
    github('rohitg00/django-blog','master')
  }
  triggers {
        githubPush()
    }
   steps {
        shell('sudo cp -rvf * /srv/djangoapp')
    }
}


job("task_job2_launchpod") {
description ("It will run the containers")
  triggers {
        upstream('task_job1_gitpull', 'SUCCESS')
    }
   steps {
        shell('''cd /srv/djangoapp
sudo docker pull rohitghumare64/devopsblog:v1
#sudo docker run -dit --name c1 --privileged -p 1234:8080  devopsblog:v1
if sudo docker ps | grep c1
then
sudo docker rm -f c1
sudo docker run -dit --name c1 --privileged -p 1234:8080 devopsblog:v1
else
#sudo docker rm -f c1
sudo docker run -dit --name c1 --privileged -p 1234:8080 devopsblog:v1
fi
''')
    }
}


job("task_job3_testapp") {
description ("It will test if conatainer is running else send a mail")

  triggers {
        upstream('task_job2_launchpod', 'SUCCESS')
    }
steps {
        shell('''if sudo docker ps | grep c1
then
echo "send to production"
else
echo "sending back to developer"
exit 1
fi''')
    }
 publishers {
        extendedEmail {
            recipientList('ghumare64@gmail.com')
            defaultSubject('Failed build')
            defaultContent('The build was failed')
            contentType('text/html')
            triggers {
                failure{
    		    attachBuildLog(true)
                    subject('Failed build')
                    content('The build was failed')
                    sendTo {
                        developers()
                        
                    }
                }
            }
        }
    }
}


job("task_job4_monitor_launch") {
description ("It will launch on production env")

  triggers {
        upstream('task_job3_testapp', 'SUCCESS')
    }
steps {
        shell('curl 127.0.0.1:8000')
    }
}


buildPipelineView('devopsorga111222') {
    title('DevOps')
    displayedBuilds(5)
    selectedJob('task_job1_gitpull')
    showPipelineParameters(true)
    refreshFrequency(3)
}
