package main

import "os"

// IsExist checks whether a file or directory exists.
// It returns false when the file or directory does not exist.
func DirExist(f string) bool {
	_, err := os.Stat(f)
	return err == nil || os.IsExist(err)
}
