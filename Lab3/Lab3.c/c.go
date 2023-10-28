package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

var table = make(map[string]bool)
var tableMutex = sync.Mutex{}
var wg sync.WaitGroup

func main() {
	wg.Add(4)
	smokerSignal := make(chan struct{}, 1)
	lastPlacements := make([]string, 0)
	go mediator(3, smokerSignal, &lastPlacements)
	go smoker("tobacco", "paper", "matches", "Smoker 0", smokerSignal, &lastPlacements)
	go smoker("paper", "matches", "tobacco", "Smoker 1", smokerSignal, &lastPlacements)
	go smoker("matches", "tobacco", "paper", "Smoker 2", smokerSignal, &lastPlacements)
	wg.Wait()
}

func mediator(variants int, smokerSignal chan struct{}, lastPlacements *[]string) {
	rand.Seed(time.Now().UnixNano())
	for {
		time.Sleep(2 * time.Second)
		ingredients := []string{"tobacco", "paper", "matches"}
		rand.Shuffle(len(ingredients), func(i, j int) {
			ingredients[i], ingredients[j] = ingredients[j], ingredients[i]
		})
		currentPlacement := fmt.Sprintf("%s and %s", ingredients[0], ingredients[1])
		if checkLastPlacements(currentPlacement, *lastPlacements) {
			continue
		}
		tableMutex.Lock()
		table[ingredients[0]] = true
		table[ingredients[1]] = true
		fmt.Printf("Mediator puts %s on the table\n", currentPlacement)
		tableMutex.Unlock()
		*lastPlacements = append(*lastPlacements, currentPlacement)

		for j := 0; j < 3; j++ {
			<-smokerSignal
		}

		time.Sleep(10 * time.Second)
	}
	wg.Done()
}

func smoker(have, ingredient1, ingredient2, name string, smokerSignal chan struct{}, lastPlacements *[]string) {
	defer wg.Done()
	for {
		tableMutex.Lock()
		_, ok1 := table[ingredient1]
		_, ok2 := table[ingredient2]
		if ok1 && ok2 {
			delete(table, ingredient1)
			delete(table, ingredient2)
			tableMutex.Unlock()
			fmt.Printf("%s got %s and %s, now making a cigarette with %s and smoking...\n", name, ingredient1, ingredient2, have)
			time.Sleep(5 * time.Second)
			fmt.Printf("%s finished smoking.\n", name)
			time.Sleep(2 * time.Second)
			smokerSignal <- struct{}{}
		} else {
			tableMutex.Unlock()
			smokerSignal <- struct{}{}
		}
	}
}

func checkLastPlacements(current string, lastPlacements []string) bool {
	for _, placement := range lastPlacements {
		if current == placement {
			return true
		}
	}
	return false
}
