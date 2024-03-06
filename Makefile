start:
	docker compose down
	docker compose up -d --wait

stop:
	docker compose down

build-image:
	./gradlew clean bootJar
	docker build . -t walkplanner-image