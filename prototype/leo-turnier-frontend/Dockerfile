### STAGE 1 ###
FROM node:14-alpine3.10 as build
WORKDIR /app
COPY package.json package-lock.json ./
RUN npm install
COPY . /app
# RUN ls
RUN npm run build --prod
### STAGE 2 ###
FROM nginx:stable-alpine
COPY nginx/nginx.conf /etc/nginx/nginx.conf
COPY --from=build /app/dist/leo-turnier-frontend /usr/share/nginx/html
EXPOSE 4200:80
CMD ["nginx", "-g", "daemon off;"]
