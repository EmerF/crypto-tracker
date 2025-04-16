variable "region" {
  default = "us-west-2"
}

variable "cluster_name" {
  default = "cryptotracker-cluster"
}

variable "vpc_cidr" {
  default = "10.0.0.0/16"
}

variable "public_subnet_cidrs" {
  default = ["10.0.1.0/24", "10.0.2.0/24"]
}

variable "node_instance_type" {
  default = "t3.micro"
}

variable "desired_capacity" {
  default = 1
}

variable "max_capacity" {
  default = 1
}

variable "min_capacity" {
  default = 1
}
