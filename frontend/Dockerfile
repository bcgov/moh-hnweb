FROM node:lts-alpine as build-stage
ARG env=production
WORKDIR /app
COPY . .
RUN yarn install
RUN yarn build --mode $env

# production stage
FROM nginxinc/nginx-unprivileged:1.20 as production-stage
USER root
WORKDIR /usr/share/nginx/html
RUN rm -rf ./*
COPY --from=build-stage /app/dist .

EXPOSE 8080
CMD ["nginx", "-g", "daemon off;"]