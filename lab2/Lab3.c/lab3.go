package main

import (
	"fmt"
	"math/rand"
	"sync"
	"time"
)

type MonkFighter struct {
	AttackPower   int
	Monastery     string
	MonkFighterID int
}

var countMonkFighters = 0
var mu sync.Mutex

func NewMonkFighter() *MonkFighter {
	mu.Lock()
	defer mu.Unlock()
	time.Sleep(10 * time.Millisecond)
	rand.Seed(time.Now().UnixNano())
	attackPower := rand.Intn(100)
	monasteryOptions := []string{"Huan-un", "Huan-in"}
	monastery := monasteryOptions[rand.Intn(len(monasteryOptions))]
	countMonkFighters++
	return &MonkFighter{AttackPower: attackPower, Monastery: monastery, MonkFighterID: countMonkFighters}
}

func (m *MonkFighter) String() string {
	return fmt.Sprintf("Monk-Fighter #%d (from %s monastery) | Power of Attack: %d", m.MonkFighterID, m.Monastery, m.AttackPower)
}

type Competition struct {
	MonkFighters []*MonkFighter
	Start        int
	End          int
}

func NewCompetition(monkFighters []*MonkFighter, start, end int) *Competition {
	return &Competition{MonkFighters: monkFighters, Start: start, End: end}
}

func (c *Competition) fight(left, right *MonkFighter) *MonkFighter {
	if left.AttackPower > right.AttackPower {
		return left
	}
	return right
}

func (c *Competition) compute() *MonkFighter {
	length := c.End - c.Start
	if length == 0 {
		return c.MonkFighters[c.Start]
	} else if length == 1 {
		return c.fight(c.MonkFighters[c.End], c.MonkFighters[c.Start])
	} else {
		mid := c.Start + (c.End-c.Start)/2
		leftChampion := NewCompetition(c.MonkFighters, c.Start, mid)
		rightChampion := NewCompetition(c.MonkFighters, mid+1, c.End)
		leftChampionResult := leftChampion.compute()
		rightChampionResult := rightChampion.compute()
		fmt.Println("Left Champion Result:", leftChampionResult.String())
		fmt.Println("Right Champion Result:", rightChampionResult.String())
		return c.fight(leftChampionResult, rightChampionResult)
	}
}

func main() {
	countMonkFighters := 33
	monkFighters := make([]*MonkFighter, countMonkFighters)
	for i := 0; i < countMonkFighters; i++ {
		monkFighters[i] = NewMonkFighter()
		fmt.Println(monkFighters[i].String())
	}
	competition := NewCompetition(monkFighters, 0, countMonkFighters-1)
	winner := competition.compute()
	fmt.Println("The winner is", winner.String())
}
