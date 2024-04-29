release:
	mvn -X release:prepare release:perform

check-dependencies:
	mvn -N versions:display-dependency-updates