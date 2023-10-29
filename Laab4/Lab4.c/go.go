package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

type Matrix struct {
	locks    [][]sync.RWMutex
	integers [][]int
}

func NewMatrix(num int) *Matrix {
	matrix := &Matrix{
		locks:    make([][]sync.RWMutex, num),
		integers: make([][]int, num),
	}
	for i := 0; i < num; i++ {
		matrix.locks[i] = make([]sync.RWMutex, num)
		matrix.integers[i] = make([]int, num)
		for j := 0; j < num; j++ {
			matrix.integers[i][j] = rand.Intn(100)
		}
	}
	return matrix
}

func (m *Matrix) getSizeOfIntegers() int {
	return len(m.integers)
}

func (m *Matrix) getInteger(i, j int) int {
	return m.integers[i][j]
}

func (m *Matrix) setInteger(i, j, value int) {
	m.integers[i][j] = value
}

func (m *Matrix) lockRead(i, j int) {
	m.locks[i][j].RLock()
}

func (m *Matrix) unlockRead(i, j int) {
	m.locks[i][j].RUnlock()
}

func (m *Matrix) lockWrite(i, j int) {
	m.locks[i][j].Lock()
}

func (m *Matrix) unlockWrite(i, j int) {
	m.locks[i][j].Unlock()
}

func (m *Matrix) lockReadAll() {
	for i := 0; i < len(m.locks); i++ {
		for j := 0; j < len(m.locks[0]); j++ {
			m.lockRead(i, j)
		}
	}
}

func (m *Matrix) unlockReadAll() {
	for i := 0; i < len(m.locks); i++ {
		for j := 0; j < len(m.locks[0]); j++ {
			m.unlockRead(i, j)
		}
	}
}

func (m *Matrix) matrixOutput() {
	for i := 0; i < len(m.integers); i++ {
		for j := 0; j < len(m.integers); j++ {
			fmt.Print(m.getInteger(i, j), " ")
		}
		fmt.Println()
	}
}

func changePrice(matrix *Matrix) {
	rand.Seed(time.Now().UnixNano())
	for {
		i := rand.Intn(matrix.getSizeOfIntegers())
		j := rand.Intn(matrix.getSizeOfIntegers())
		priceToChange := rand.Intn(100)

		matrix.lockWrite(i, j)
		fmt.Println("Changing price.")
		matrix.setInteger(i, j, priceToChange)
		fmt.Println("Changing price from ", i, " to ", j, " is ", priceToChange)
		fmt.Println("Finished to change price")
		matrix.unlockWrite(i, j)

		time.Sleep(800 * time.Millisecond)
	}
}

func addingDeletingWay(matrix *Matrix) {
	rand.Seed(time.Now().UnixNano())
	for {
		i := rand.Intn(matrix.getSizeOfIntegers())
		j := rand.Intn(matrix.getSizeOfIntegers())
		addBool := rand.Intn(2)

		if addBool == 1 {
			matrix.lockWrite(i, j)
			fmt.Println("Add/Delete way: Start")
			newPrice := rand.Intn(100)
			matrix.setInteger(i, j, newPrice)
			fmt.Println("Changing price: Added from ", i, " to ", j, " is ", newPrice)
			matrix.unlockWrite(i, j)
		} else {
			matrix.lockWrite(i, j)
			fmt.Println("Add/Delete way: Start")
			matrix.setInteger(i, j, -1)
			fmt.Println("Changing price: Deleted from ", i, " to ", j)
			matrix.unlockWrite(i, j)
		}

		time.Sleep(600 * time.Millisecond)
	}
}

func showPrice(matrix *Matrix) {
	rand.Seed(time.Now().UnixNano())
	for {
		i := rand.Intn(matrix.getSizeOfIntegers())
		j := rand.Intn(matrix.getSizeOfIntegers())

		matrix.lockRead(i, j)
		fmt.Println("Price: Start")
		fmt.Println("Price: from ", i, " to ", j, " costs = ", matrix.getInteger(i, j))
		fmt.Println("Price: End")
		matrix.unlockRead(i, j)

		time.Sleep(400 * time.Millisecond)
	}
}

func showMatrix(matrix *Matrix) {
	for {
		matrix.lockReadAll()
		matrix.matrixOutput()
		matrix.unlockReadAll()
		time.Sleep(200 * time.Millisecond)
	}
}

func main() {
	matrix := NewMatrix(5)

	wg := sync.WaitGroup{}
	wg.Add(4)

	go func() {
		defer wg.Done()
		changePrice(matrix)
	}()
	go func() {
		defer wg.Done()
		addingDeletingWay(matrix)
	}()
	go func() {
		defer wg.Done()
		showPrice(matrix)
	}()
	go func() {
		defer wg.Done()
		showMatrix(matrix)
	}()

	wg.Wait()
}
