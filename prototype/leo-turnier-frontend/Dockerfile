### STAGE 1 ###
FROM node:14-alpine3.10 as build
WORKDIR /app
COPY package.json package-lock.json ./
COPY . .
RUN npm install
RUN npm run build --prod
### STAGE 2 ###
#FROM nginx:stable-alpine
#COPY --from=build /app/dist/leo-turnier-frontend /etc/nginx/html
#EXPOSE 80
#CMD ["nginx", "-g", "daemon off;"]
