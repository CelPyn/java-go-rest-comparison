package main

import (
	"encoding/json"
	"fmt"
	"log"
	"net/http"
	"time"
)

type message struct {
	Content string    `json:"content"`
	Time    time.Time `json:"time"`
}

func main() {

	mux := http.NewServeMux()
	mux.Handle("/hello", loggingHandler(Get(helloHandler(nil))))

	log.Println("Listening...")
	log.Fatal(http.ListenAndServe(":8080", mux))
}

func loggingHandler(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		log.Println(fmt.Sprintf("%s - %s", r.Method, r.URL))
		next.ServeHTTP(w, r)
	})
}

func Get(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		if r.Method != http.MethodGet {
			w.WriteHeader(http.StatusMethodNotAllowed)
			return
		}
		next.ServeHTTP(w, r)
	})
}

func helloHandler(next http.Handler) http.Handler {
	return http.HandlerFunc(func(w http.ResponseWriter, r *http.Request) {
		message := message{
			Content: "Hello!",
			Time:    time.Now(),
		}
		res, _ := json.Marshal(message)
		fmt.Fprintf(w, string(res))
		if next != nil {
			next.ServeHTTP(w, r)
		}
	})
}
