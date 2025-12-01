FROM nginx:alpine AS prod

WORKDIR /usr/share/nginx/html

COPY . .

EXPOSE 8080

CMD ["nginx", "-g", "daemon off;"]