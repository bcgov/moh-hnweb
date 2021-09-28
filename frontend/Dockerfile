FROM node:lts-alpine as build-stage
WORKDIR /app
COPY . .
RUN yarn install
RUN yarn build --mode test

# production stage
FROM nginx:stable-alpine as production-stage
WORKDIR /usr/share/nginx/html
RUN rm -rf ./*
COPY --from=build-stage /app/dist .
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]