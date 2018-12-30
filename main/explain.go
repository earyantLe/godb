package main

import (
	"strings"
)

func explainType(cmd string) CmdType {
	cmd = strings.TrimSpace(cmd)
	print(cmd)
	if strings.HasPrefix(cmd, "CREATE TABLE") {
		return createTable
	} else if strings.HasPrefix(cmd, "CREATE DATABASE") {
		return createDb
	}
	return 1
}
