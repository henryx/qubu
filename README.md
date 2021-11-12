# QuBu

QUery BUilder is a Java query builder inspired from [PyPika](https://github.com/kayak/pypika). It supports:

- Simple query filters like equal, not equal
- Range filters like BETWEEN, IN, NOT IN
- Criteria filter's method like AND, OR
- GROUP BY using standard functions for aggregation like COUNT(), SUM() and so on

Intention of the library is to generate a valid SQL query. Is not an ORM and is not intended to use it for validating
query input

## Compatibility

This library is written for Java 8 or greater

## Install

With maven, you should compile with:

```
mvn clean install
```

After compiled, you can add it in your `pom.xml` these lines:

```xml

<dependency>
    <groupId>org.library</groupId>
    <artifactId>qubu</artifactId>
    <version>0.5.0</version>
</dependency>
```

## Queries

Queries are generated by `Query` class. Usage is simple:

```java
String query=Query.from("test")
        .select("t1","t2")
        .getSql();
```

That generates the following SQL:

```sql
SELECT t1, t2
FROM test
```

Query can be ordered using `Query.orderBy` method:

```java
String q=String query=Query.from("test")
        .select("t1","t2")
        .orderBy("t1")
        .getSql();
```

```sql
SELECT t1, t2
FROM test
ORDER BY t1
```

### Filters

Data can be filtered via `Query.where` method:

```java
String query=Query.from("test")
        .where(Criterion.eq("t1","t2"))
        .select("t1","t2")
        .getSql();
```

```sql
SELECT t1, t2
FROM test
WHERE t1 = t2ø
```

`Query.where` method can be repeated:

```java
String query=Query.from("test")
        .where(Criterion.eq("t1","t2"))
        .where(Criterion.eq("t1","?"))
        .select("t1","t2")
        .getSql();
```

```sql
SELECT t1, t2
FROM test
WHERE t1 = t2
  AND t1 = ?
```

### Criteria

All criteria filters are implemented via `Criterion` class and applied at `Query.where` method:

```java
String query=Query.from("test")
        .where(Criterion.eq("t1","t2"))
        .select("t1","t2")
        .getSql();
```

Generated SQL is:

```sql
SELECT t1, t2
FROM test
WHERE t1 = t2
```

`Criterion` class implements also a criterion method used to apply filter. Criterion method can be `AND` or `OR`:

```java
String q=String query=Query.from("test")
        .where(Criterion.eq("t1","?"))
        .where(Criterion.eq("t1","?").method(Criterion.OR))
        .where(Criterion.neq("t2","?").method(Criterion.AND))
        .select("t1","t2")
        .getSql();
```

[f]: @formatter:off
```sql
SELECT t1, t2
FROM test
WHERE t1 = ?
   OR t1 = ?
  AND t2 != ?
```
[f]: @formatter:on

Criteria filters implemented are:

#### Equality

```java
String query=Query.from("test")
        .where(Criterion.eq("t1","t2"))
        .select("t1","t2")
        .getSql();
```

```sql
SELECT t1, t2
FROM test
WHERE t1 = t2
```

#### Not equality

```java
String query=Query.from("test")
        .where(Criterion.neq("t1","t2"))
        .select("t1","t2")
        .getSql();
```

```sql
SELECT t1, t2
FROM test
WHERE t1 != t2
```

#### Greater than

```java
String query=Query.from("test")
        .where(Criterion.gt("t1","t2"))
        .select("t1","t2")
        .getSql();
```

```sql
SELECT t1, t2
FROM test
WHERE t1 > t2
```

#### Less than

```java
String query=Query.from("test")
        .where(Criterion.lt("t1","t2"))
        .select("t1","t2")
        .getSql();
```

```sql
SELECT t1, t2
FROM test
WHERE t1 < t2
```

#### Less or equal than

```java
String query=Query.from("test")
        .where(Criterion.lte("t1","t2"))
        .select("t1","t2")
        .getSql();
```

```sql
SELECT t1, t2
FROM test
WHERE t1 <= t2
```

#### Is null

```java
String query=Query.from("test")
        .where(Criterion.isNull("t1"))
        .select("t1","t2")
        .getSql();
```

```sql
SELECT t1, t2
FROM test
WHERE t1 IS NULL
```

#### Is not null

```java
String query=Query.from("test")
        .where(Criterion.isNotNull("t1"))
        .select("t1","t2")
        .getSql();
```

```sql
SELECT t1, t2
FROM test
WHERE t1 IS NOT NULL
```

#### In

```java
String query=Query.from("test")
        .where(Criterion.in("t1","1","2","3"))
        .select("t1","t2")
        .getSql();
```

```sql
SELECT t1, t2
FROM test
WHERE t1 IN (1, 2, 3)
```

#### Not in

```java
String query=Query.from("test")
        .where(Criterion.nin("t1","1","2","3"))
        .select("t1","t2")
        .getSql();
```

```sql
SELECT t1, t2
FROM test
WHERE t1 NOT IN (1, 2, 3)
```

#### Subqueries

Is it possible to define a subquery as filter in all criteria methods:

```java
String query=Query.from("test")
        .select("t1","t2")
        .where(Criterion.gte("t1",Query.from("test").select("a2").where(Criterion.neq("a1","3"))))
        .getSql();
```

```sql
SELECT t1, t2
FROM test
WHERE t1 >= (SELECT a2 FROM test WHERE a1 != 3)
```

### Aggregations

`Query.groupBy` mehtod implements aggregation methods described in `Functions` class:

#### Count

```java
String query=Query.from("test")
        .where(Criterion.neq("t1","t2"))
        .select("t1","t2",Functions.count("t3").getSql())
        .groupBy("t1","t2")
        .getSql();
```

```sql
SELECT t1, t2, COUNT(t3)
FROM test
WHERE t1 != t2
GROUP BY t1, t2
```

#### Sum

```java
String query=Query.from("test")
        .where(Criterion.neq("t1","t2"))
        .select("t1","t2",Functions.sum("t3").getSql())
        .groupBy("t1","t2")
        .getSql();
```

```sql
SELECT t1, t2, SUM(t3)
FROM test
WHERE t1 != t2
GROUP BY t1, t2
```

#### Average

```java
String query=Query.from("test")
        .where(Criterion.neq("t1","t2"))
        .select("t1","t2",Functions.avg("t3").getSql())
        .groupBy("t1","t2")
        .getSql();
```

```sql
SELECT t1, t2, AVG(t3)
FROM test
WHERE t1 != t2
GROUP BY t1, t2
```

#### Minimum

```java
String query=Query.from("test")
        .where(Criterion.neq("t1","t2"))
        .select("t1","t2",Functions.min("t3").getSql())
        .groupBy("t1","t2")
        .getSql();
```

```sql
SELECT t1, t2, MIN(t3)
FROM test
WHERE t1 != t2
GROUP BY t1, t2
```

#### Maximum

```java
String query=Query.from("test")
        .where(Criterion.neq("t1","t2"))
        .select("t1","t2",Functions.max("t3").getSql())
        .groupBy("t1","t2")
        .getSql();
```

```sql
SELECT t1, t2, MAX(t3)
FROM test
WHERE t1 != t2
GROUP BY t1, t2
```

#### Having

With `Query.having` method is possible to implement a `HAVING` filter:

```java
String query=Query.from("test")
        .where(Criterion.neq("t1","t2"))
        .select("t1","t2",Functions.count("t3").getSql())
        .groupBy("t1","t2")
        .having(Criterion.gte(Functions.count("t3").getSql(),"1000"))
        .getSql();
```

[intellij]: @formatter:off
```sql
SELECT t1, t2, COUNT(t3) FROM test WHERE t1 != t2 GROUP BY t1, t2 HAVING COUNT(t3) >= 1000 AND COUNT(t1) < 10
```
[intellij]: @formatter:on

As viewed in [WHERE](#filters) clause, `Query.having` can be repeated multiple times using `Criterion` class logic