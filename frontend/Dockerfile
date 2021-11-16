FROM node:lts-alpine as build-stage
ARG env=test
WORKDIR /app


# Copying pacakge before other code. 
# This will cache the dependencies
COPY package*.json  ./
RUN yarn install

# Copy rest of the files
COPY . .
RUN yarn build --mode $env

# production stage
FROM nginxinc/nginx-unprivileged:1.20 as production-stage
USER root
COPY .nginx/nginx.conf /etc/nginx/nginx.conf

WORKDIR /usr/share/nginx/html
RUN rm -rf ./*
COPY --from=build-stage /app/dist .

EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]