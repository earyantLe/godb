package main

func main() {
	var dbName = "db"
	var cmd = "CREATE DATABASE " + dbName
	runShell(dbName, cmd)
	cmd = "CREATE TABLE `feature_conf` (\n          `id` varchar(255) DEFAULT NULL,\n          PRIMARY KEY (`id`)\n)"
	runShell(dbName, cmd)
}

func runShell(dbName string, cmd string) {
	//	语义分析层
	var cmdType CmdType
	cmdType = explainType(cmd)
	//  shell执行层
	if cmdType == createDb {
		//	 创建数据库目录
	} else if cmdType == createTable {
		//创建表目录

	}

}
