nohup minikube kubectl --  port-forward -n kubernetes-dashboard --address 0.0.0.0 service/kubernetes-dashboard 8001:443 > kubernetes-dashboard.log 2>&1 &
