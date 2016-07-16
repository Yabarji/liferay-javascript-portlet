
## Apache HTTPD <a name="apache"></a>

#### Apache virtual host configuration file
```
<VirtualHost *:80>
  ProxyRequests Off
  ProxyPreserveHost On
  RewriteEngine On

  RewriteRule ^/web/guest/home/(.*)$ /web/guest/home [L,P]

  # Liferay AJP runs on port 8009
  ProxyPass / ajp://localhost:8009/
  ProxyPassReverse / ajp://localhost:8009/

</VirtualHost>
```

## Nginx <a name="nginx"></a> (coming up)