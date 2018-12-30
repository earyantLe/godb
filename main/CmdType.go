package main

type CmdType int

const (
	createDb    CmdType = iota // value --> 0
	createTable                // value --> 1
)

func (this CmdType) String() string {
	switch this {
	case createDb:
		return "Running"
	case createTable:
		return "Stopped"
	default:
		return "Unknow"
	}
}
