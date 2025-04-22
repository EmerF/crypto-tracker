variable "cluster_name" {
  description = "EKS cluster name"
  type        = string
}

variable "cluster_role_arn" {
  description = "ARN of the IAM role for the cluster"
  type        = string
}

variable "subnet_ids" {
  description = "Subnets to deploy EKS nodes"
  type        = list(string)
}

variable "node_role_arn" {
  description = "ARN of the IAM role for the node group"
  type        = string
}

variable "node_instance_type" {
  description = "EC2 instance type for the node group"
  type        = string
}

variable "desired_capacity" {
  description = "Desired number of nodes in the node group"
  type        = number
}

variable "max_capacity" {
  description = "Max number of nodes in the node group"
  type        = number
}

variable "min_capacity" {
  description = "Min number of nodes in the node group"
  type        = number
}
