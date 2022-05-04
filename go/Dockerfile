FROM golang:1.18.1 as build

WORKDIR /src
COPY . .
RUN CGO_ENABLED=0 GOOS=linux go build -o app .

FROM alpine:latest

WORKDIR /go
COPY --from=build /src/app /go/app
EXPOSE 8080
ENTRYPOINT /go/app