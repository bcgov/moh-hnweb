FROM node:lts-alpine as build-stage
ARG env=production
RUN echo 'Logged in user:'
RUN whoami

WORKDIR /app
COPY . .
RUN yarn install
RUN yarn build --mode $env

# production stage
FROM nginxinc/nginx-unprivileged:1.20 as production-stage
#RUN mkdir /tmp/frontend-code
#COPY --from=build-stage /app/dist /tmp/frontend-code
#RUN chmod g+rwx /var/cache/nginx /var/run /var/log/nginx

WORKDIR /usr/share/nginx/html
#RUN rm -rf ./*
COPY --from=build-stage /app/dist .

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]