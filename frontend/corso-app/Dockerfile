# Use the official Node.js image as a base image
FROM node:18-alpine as build

# Set the working directory in the container
WORKDIR /app

# Copy the package.json and package-lock.json files to the container
COPY package*.json ./

# Install dependencies
RUN npm install

# Copy the rest of the application code to the container, excluding node_modules
COPY . .

# Build the application
RUN npm run build

# Use Nginx to serve the built files
FROM nginx:alpine

# Copy the built files from the previous stage to the Nginx web root
COPY --from=build /app/dist /usr/share/nginx/html

# Expose the port that Nginx is running on
EXPOSE 5173

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]
