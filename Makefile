run-tests-jdk-7:
	docker build -f Dockerfile.jdk7.test -t timewords:jdk7 . && docker run timewords:jdk7

run-tests-jdk-8:
	docker build -f Dockerfile.jdk8.test -t timewords:jdk8 . && docker run timewords:jdk8

run-tests-jdk-10:
	docker build -f Dockerfile.jdk10.test -t timewords:jdk10 . && docker run timewords:jdk10

run-tests-jdk-11:
	docker build -f Dockerfile.jdk11.test -t timewords:jdk11 . && docker run timewords:jdk11

run-tests-from-8-to-11-till-fail:
	make run-tests-jdk-8 && make run-tests-jdk-10 && make run-tests-jdk-11
