FROM ubuntu/nginx

# Copy the Nginx config
COPY volumes/nginx/default.conf /etc/nginx/conf.d/
COPY volumes/nginx/nginx.conf /etc/nginx/
COPY volumes/nginx/index.html /usr/share/nginx/html/

# Expose the port for access
EXPOSE 8080/tcp

# Run the Nginx server
ENTRYPOINT ["/usr/sbin/nginx", "-g", "daemon off;"]