FROM node:lts-alpine as build-stage
ARG env=production
WORKDIR /app
COPY . .
RUN yarn install
RUN yarn build --mode $env

# production stage
#FROM nginx:stable-alpine as production-stage
#Trying underpriv niginx build
FROM nginxinc/nginx-unprivileged:1.20 as production-stage
WORKDIR /usr/share/nginx/html
#RUN rm -rf ./*
COPY --from=build-stage /app/dist .
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]