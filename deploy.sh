#!/bin/bash

# CloudDBService EC2 Deployment Script
echo "🚀 Deploying CloudDBService to EC2..."

# Update system
echo "📦 Updating system packages..."
sudo yum update -y

# Install Java 17
echo "☕ Installing Java 17..."
sudo yum install java-17-amazon-corretto-devel -y

# Install Maven
echo "🔨 Installing Maven..."
sudo yum install maven -y

# Create app directory
echo "📁 Creating application directory..."
mkdir -p /home/ec2-user/clouddb
cd /home/ec2-user/clouddb

# Build application (assuming source code is already uploaded)
echo "🏗️ Building application..."
mvn clean package -DskipTests

# Create systemd service
echo "⚙️ Creating systemd service..."
sudo tee /etc/systemd/system/clouddb.service << 'EOF'
[Unit]
Description=CloudDBService Spring Boot Application
After=network.target

[Service]
Type=simple
User=ec2-user
WorkingDirectory=/home/ec2-user/clouddb
ExecStart=/usr/bin/java -jar /home/ec2-user/clouddb/target/CloudDBService-0.0.1-SNAPSHOT.jar
Restart=always
RestartSec=10
Environment=MONGODB_URI=mongodb://localhost:27017/clouddb
Environment=MONGODB_DATABASE=clouddb

[Install]
WantedBy=multi-user.target
EOF

# Enable and start service
echo "🔄 Starting service..."
sudo systemctl daemon-reload
sudo systemctl enable clouddb
sudo systemctl start clouddb

# Check status
echo "✅ Checking service status..."
sudo systemctl status clouddb

echo "🎉 Deployment complete! Your app should be running on port 8080"
echo "🌐 Test with: curl http://localhost:8080/api/user/register -H 'Content-Type: application/json' -d '{\"name\":\"Test\",\"email\":\"test@example.com\"}'"
