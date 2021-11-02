FROM node:lts-alpine as build-stage
ARG env=production
WORKDIR /app
COPY . .
RUN yarn install
RUN yarn build --mode $env

# production stage
FROM nginx:stable-alpine as production-stage
RUN mkdir /tmp/frontend-code
COPY --from=build-stage /app/dist /tmp/frontend-code

WORKDIR /usr/share/nginx/html
RUN rm -rf ./*
RUN cp -ar /tmp/frontend-code/. .
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]