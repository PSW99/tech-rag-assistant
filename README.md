# Tech RAG Assistant

기술 블로그/문서를 저장하고, 자연어로 질문하면 RAG 기반으로 답변해주는 서비스

## 기술 스택

- **Backend**: Spring Boot 3.5, Java 17
- **Database**: PostgreSQL + pgvector
- **Queue**: Redis
- **AI**: OpenAI API (Embedding, Chat)

## 아키텍처
```
Chrome Extension → Spring Boot API → Redis Queue
                                          ↓
                        PostgreSQL(pgvector) ← OpenAI Embedding
```

## 주요 기능

- [ ] 텍스트 저장 및 임베딩
- [ ] 벡터 유사도 검색
- [ ] RAG 기반 답변 생성

## 실행 방법
```bash
./gradlew bootRun
```