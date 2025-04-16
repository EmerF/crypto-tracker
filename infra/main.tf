provider "aws" {
  region = var.region
}

module "vpc" {
  source  = "terraform-aws-modules/vpc/aws"
  version = "5.1.1"

  name                 = "cryptotracker-vpc"
  cidr                 = var.vpc_cidr
  azs                  = ["${var.region}a", "${var.region}b"]
  public_subnets       = var.public_subnet_cidrs
  enable_nat_gateway   = false
  single_nat_gateway   = false
  enable_dns_hostnames = true
  enable_dns_support   = true

  tags = {
    Name = "cryptotracker-vpc"
  }
}
