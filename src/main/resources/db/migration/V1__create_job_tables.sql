CREATE TABLE endpoints (
    id UUID PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    url TEXT NOT NULL,
    created_at TIMESTAMPTZ NOT NULL
);

CREATE TABLE jobs (
    id UUID PRIMARY KEY,
    endpoint_id UUID NOT NULL,
    payload TEXT NOT NULL,
    status VARCHAR(20) NOT NULL,
    available_at TIMESTAMPTZ NOT NULL,
    attempt_count INTEGER NOT NULL DEFAULT 0,
    created_at TIMESTAMPTZ NOT NULL,

    CONSTRAINT fk_jobs_endpoint
        FOREIGN KEY (endpoint_id)
        REFERENCES endpoints (id)
);

CREATE INDEX idx_jobs_queue
    ON jobs (status, available_at);