# Pattern index — 1p3a question bank

Every question in `1p3a-qbanks/`, bucketed by primary pattern (first match on its tags, most specific first). Generated; companies listed by question count.

| Pattern | Questions | High/Very-high freq | Top companies |
|---|---|---|---|
| system-design | 302 | 17 | OpenAI 18, Apple 17, Roblox 16, Meta 14, Amazon 13 |
| graph | 292 | 12 | Uber 32, Apple 20, Google 19, Snowflake 18, ByteDance / TikTok 17 |
| stateful-oop-design | 154 | 14 | Amazon 10, Anthropic 8, OpenAI 8, Uber 8, Airbnb 7 |
| window-pointers-prefix | 118 | 3 | Amazon 9, ByteDance / TikTok 9, Uber 9, Apple 8, Goldman Sachs 7 |
| parsing-simulation | 110 | 6 | Stripe 11, Capital One 9, Google 6, JPMorgan Chase 5, Amazon 4 |
| ml-coding | 94 | 9 | Amazon 10, OpenAI 8, Apple 7, Tesla 6, Capital One 5 |
| [intervals-scheduling](intervals/README.md) | 87 | 6 | Stripe 8, ByteDance / TikTok 5, Roblox 5, Snowflake 5, Airbnb 4 |
| heap-topk | 76 | 0 | Amazon 7, Apple 7, Google 5, Snowflake 5, Uber 4 |
| behavioral | 72 | 44 | Anthropic 4, ByteDance / TikTok 4, Airbnb 3, Amazon 3, OpenAI 3 |
| dp-backtracking | 67 | 3 | Amazon 6, Google 6, Uber 6, Airbnb 4, Pinterest 4 |
| math-greedy-sort | 65 | 4 | Optiver 6, Akuna Capital 5, Amazon 5, Goldman Sachs 5, Capital One 4 |
| coding-unclassified | 65 | 4 | Stripe 7, Anduril Industries 5, Anthropic 4, SoFi 4, Coinbase 3 |
| hashing-counting | 64 | 1 | Amazon 8, Goldman Sachs 7, Apple 6, Google 4, Affirm 3 |
| tree-trie | 61 | 2 | Google 5, Amazon 4, Apple 4, Snowflake 4, Databricks 3 |
| concurrency | 60 | 5 | Apple 4, Microsoft 4, xAI 4, Databricks 3, LinkedIn 3 |
| stack-queue | 52 | 2 | ByteDance / TikTok 4, Amazon 3, Google 3, Meta 3, Snowflake 3 |
| other | 50 | 9 | Capital One 5, Optiver 4, Oracle 4, Amazon 3, ByteDance / TikTok 3 |
| linked-list | 40 | 1 | Apple 6, ByteDance / TikTok 3, Amazon 2, Cisco 2, LinkedIn 2 |
| binary-search | 35 | 1 | Uber 4, Apple 3, ByteDance / TikTok 2, Citadel / Citadel Securities 2, LinkedIn 2 |
| array-string-misc | 29 | 0 | Stripe 5, Amazon 4, Capital One 2, Goldman Sachs 2, Google 2 |

## Questions by pattern

<details><summary><b>system-design</b> (302)</summary>

| Question | Company | Freq | Level |
|---|---|---|---|
| [System Design Q1 — Inference API / Batched GPU Serving](../1p3a-qbanks/anthropic/raw/sd-q1-inference-api.txt) | Anthropic | Very high | hard |
| [Design Chess.com (Online Chess Game)](../1p3a-qbanks/openai/raw/design-chess-game.txt) | OpenAI | Very high | – |
| [Design Sora / Video Generation Pipeline](../1p3a-qbanks/openai/raw/design-sora-video-generation.txt) | OpenAI | Very high | medium |
| [Payment / Coffee-Shop Ordering (read the prompt!)](../1p3a-qbanks/openai/raw/payment-coffee-shop.txt) | OpenAI | Very high | medium |
| [Google News / News Aggregator](../1p3a-qbanks/rippling/raw/google-news-aggregator.txt) | Rippling | Very high | hard |
| [Onsite Design Doc Review](../1p3a-qbanks/anthropic/raw/onsite-design-doc-review.txt) | Anthropic | High | medium |
| [System Design Q2 — Prompt Playground (Product Design)](../1p3a-qbanks/anthropic/raw/sd-q2-prompt-playground.txt) | Anthropic | High | medium |
| [System Design Q3 — Model Weight Distribution](../1p3a-qbanks/anthropic/raw/sd-q3-model-distribution.txt) | Anthropic | High | medium |
| [System Design Q4 — 1-on-1 Chat System](../1p3a-qbanks/anthropic/raw/sd-q4-1to1-chat-system.txt) | Anthropic | High | medium |
| [Book Seller External Fan-Out](../1p3a-qbanks/databricks/raw/book-seller-external-fanout.txt) | Databricks | High | medium |
| [System Design: 3-Day Donation Service](../1p3a-qbanks/doordash/raw/system-design-three-day-donation.txt) | DoorDash | High | medium |
| [System Design: Food Review System with Reward Bonus](../1p3a-qbanks/doordash/raw/system-design-food-review-system.txt) | DoorDash | High | medium |
| [ML System Design (Recsys / Chatbot / Image Classifier)](../1p3a-qbanks/google/raw/ml-system-design-recsys-chatbot-classifier.txt) | Google | High | – |
| [Design Google Drive / Data Room](../1p3a-qbanks/harvey/raw/design-google-drive-data-room.txt) | Harvey | High | medium |
| [Ads Demand Intake Data Modeling](../1p3a-qbanks/netflix/raw/ads-demand-data-modeling.txt) | Netflix | High | hard |
| [Ads Frequency Cap / Limiter](../1p3a-qbanks/netflix/raw/ads-frequency-cap-system-design.txt) | Netflix | High | hard |
| [ML Modeling Round (Forecasting / Targeting / Fraud)](../1p3a-qbanks/shopify/raw/ml-modeling-round.txt) | Shopify | High | – |
| [Design an A/B Testing / Experimentation Platform](../1p3a-qbanks/affirm/raw/ab-testing-platform-design.txt) | Affirm | Medium | – |
| [Distributed Key-Value Store Design](../1p3a-qbanks/airbnb/raw/distributed-key-value-store-design.txt) | Airbnb | Medium | medium |
| [Group Chat / Messaging System Design](../1p3a-qbanks/airbnb/raw/group-chat-messaging-system-design.txt) | Airbnb | Medium | medium |
| [Pizza / Restaurant System (OOD)](../1p3a-qbanks/amazon/raw/pizza-restaurant-ood.txt) | Amazon | Medium | medium |
| [Object Storage / Dropbox / S3 Design](../1p3a-qbanks/apple/raw/object-storage-dropbox-s3-design.txt) | Apple | Medium | medium |
| [Image Link Crawler Service](../1p3a-qbanks/atlassian/raw/image-link-crawler-service.txt) | Atlassian | Medium | medium |
| [RAG Search Augmentation and Internal Chatbot](../1p3a-qbanks/atlassian/raw/rag-search-augmentation-internal-chatbot.txt) | Atlassian | Medium | medium |
| [Content Moderation System Design](../1p3a-qbanks/bytedance/raw/content-moderation-system-design.txt) | ByteDance / TikTok | Medium | hard |
| [Online Banking Application (System Design)](../1p3a-qbanks/capitalone/raw/online-banking-application.txt) | Capital One | Medium | – |
| [System Design — Coinbase Explore (Real-Time Price Dashboard)](../1p3a-qbanks/coinbase/raw/sd-coinbase-explore-realtime-price.txt) | Coinbase | Medium | medium |
| [Chat system similar to Slack](../1p3a-qbanks/databricks/raw/slack-chat-system.txt) | Databricks | Medium | medium |
| [In-Memory KV Cache With WAL](../1p3a-qbanks/databricks/raw/in-memory-kv-cache-with-wal.txt) | Databricks | Medium | medium |
| [ML System Design: Restaurant / Store Recommendation](../1p3a-qbanks/doordash/raw/mle-system-design-restaurant-recommendation.txt) | DoorDash | Medium | medium |
| [System Design: Notification / Alert System](../1p3a-qbanks/doordash/raw/system-design-notification-alert-system.txt) | DoorDash | Medium | medium |
| [Real-Time Comment Threads (FigJam / Figma)](../1p3a-qbanks/figma/raw/realtime-comments-design.txt) | Figma | Medium | – |
| [Trending System / Top-K Hot Files](../1p3a-qbanks/figma/raw/trending-top-k-hot-files.txt) | Figma | Medium | – |
| [L6 System Design (Staff Loop)](../1p3a-qbanks/google/raw/l6-system-design-staff-loop.txt) | Google | Medium | – |
| [URL Shortener With Click Tracking](../1p3a-qbanks/jpmorgan/raw/url-shortener-with-click-tracking.txt) | JPMorgan Chase | Medium | medium |
| [AI-Powered Personalized Recruiter Message Generation](../1p3a-qbanks/linkedin/raw/sd-personalized-inmail-ai.txt) | LinkedIn | Medium | medium |
| [Distributed Job Scheduler / Calendar Service](../1p3a-qbanks/linkedin/raw/sd-job-scheduler.txt) | LinkedIn | Medium | medium |
| [Metrics & Monitoring Platform](../1p3a-qbanks/linkedin/raw/sd-metrics-monitoring-platform.txt) | LinkedIn | Medium | medium |
| [Wikipedia Internet Archive / Web Crawler](../1p3a-qbanks/lyft/raw/wikipedia-internet-archive-crawler.txt) | Lyft | Medium | medium |
| [AI-Enabled System Design — Agent System](../1p3a-qbanks/meta/raw/system-design-ai-enabled-agent-system.txt) | Meta | Medium | hard |
| [MLSD — Ads Ranking](../1p3a-qbanks/meta/raw/mlsd-ads-ranking.txt) | Meta | Medium | hard |
| [MLSD — Harmful / Weapon-Sales Content Detection](../1p3a-qbanks/meta/raw/mlsd-harmful-content-detection.txt) | Meta | Medium | hard |
| [MLSD — Nearby Place Recommendation](../1p3a-qbanks/meta/raw/mlsd-nearby-place-recommendation.txt) | Meta | Medium | medium |
| [MLSD — Reels / Short Video Recommendation](../1p3a-qbanks/meta/raw/mlsd-reels-short-video-recommendation.txt) | Meta | Medium | hard |
| [System Design — Facebook Search (Mini Elasticsearch)](../1p3a-qbanks/meta/raw/system-design-facebook-search.txt) | Meta | Medium | medium |
| [System Design — Instagram / News Feed](../1p3a-qbanks/meta/raw/system-design-instagram-news-feed.txt) | Meta | Medium | medium |
| [System Design — Online Auction / eBay](../1p3a-qbanks/meta/raw/system-design-online-auction.txt) | Meta | Medium | medium |
| [Rate Limiter (Design + Implementation)](../1p3a-qbanks/microsoft/raw/rate-limiter-design-and-implementation.txt) | Microsoft | Medium | medium |
| [Design a Cloud IDE](../1p3a-qbanks/openai/raw/design-cloud-ide.txt) | OpenAI | Medium | hard |
| [ML System Design: Notification Ranking & Ads CTR](../1p3a-qbanks/pinterest/raw/ml-system-design-notifications-ctr.txt) | Pinterest | Medium | hard |
| [ML System Design: Search & Ranking](../1p3a-qbanks/pinterest/raw/ml-system-design-search-and-ranking.txt) | Pinterest | Medium | hard |
| [System Design: Search Typeahead / Input Suggestions](../1p3a-qbanks/pinterest/raw/system-design-typeahead-suggestions.txt) | Pinterest | Medium | medium |
| [Hotel Booking System](../1p3a-qbanks/rippling/raw/hotel-booking-system.txt) | Rippling | Medium | hard |
| [User Behavior / Metrics Monitoring Aggregator](../1p3a-qbanks/rippling/raw/user-behavior-monitoring-aggregator.txt) | Rippling | Medium | hard |
| [Job Scheduler System Design](../1p3a-qbanks/robinhood/raw/job-scheduler-design.txt) | Robinhood | Medium | – |
| [Stock Trading / Real-Time Quote System Design](../1p3a-qbanks/robinhood/raw/stock-trading-quote-system-design.txt) | Robinhood | Medium | – |
| [Delayed Payment Scheduler (Robux Transfer)](../1p3a-qbanks/roblox/raw/delayed-payment-scheduler.txt) | Roblox | Medium | hard |
| [Like / Unlike Counter System](../1p3a-qbanks/roblox/raw/like-unlike-counter-system.txt) | Roblox | Medium | hard |
| [Matchmaking Service for Multiplayer Games](../1p3a-qbanks/roblox/raw/matchmaking-service-multiplayer.txt) | Roblox | Medium | hard |
| [Coffee Ordering System Design](../1p3a-qbanks/salesforce/raw/coffee-ordering-system.txt) | Salesforce | Medium | medium |
| [ML System Design: Product Categorization / Taxonomy](../1p3a-qbanks/shopify/raw/ml-system-design-product-categorization.txt) | Shopify | Medium | – |
| [Metrics Collection, Alerting & Click Counters](../1p3a-qbanks/snapchat/raw/metrics-collection-alerting-and-click-counters.txt) | Snapchat | Medium | medium |
| [Short Video Recommendation & Ranking](../1p3a-qbanks/snapchat/raw/short-video-recommendation-and-ranking.txt) | Snapchat | Medium | hard |
| [Snap Story or Spotlight Feed Backend](../1p3a-qbanks/snapchat/raw/snap-story-or-spotlight-feed-backend.txt) | Snapchat | Medium | medium |
| [Job Scheduler with Cron / Pause / Resume](../1p3a-qbanks/snowflake/raw/job-scheduler-cron-pause-resume.txt) | Snowflake | Medium | medium |
| [Design a Ledger Service (Record Transaction + Merchant Balance)](../1p3a-qbanks/stripe/raw/ledger-record-transaction-design.txt) | Stripe | Medium | hard |
| [MLE Onsite: ML System Design (Recommendation / Feed Ranking / ETA)](../1p3a-qbanks/uber/raw/mle-onsite-ml-system-design.txt) | Uber | Medium | hard |
| [Onsite System Design: Driver Location Heatmap](../1p3a-qbanks/uber/raw/onsite-sd-driver-location-heatmap.txt) | Uber | Medium | hard |
| [Onsite System Design: Top-K Popular Items (Restaurant / Search)](../1p3a-qbanks/uber/raw/onsite-sd-top-k-popular-items.txt) | Uber | Medium | hard |
| [DAU / MAU Internal Analytics System](../1p3a-qbanks/vanta/raw/dau-mau-analytics-system.txt) | Vanta | Medium | – |
| [Airbnb Booking System Design](../1p3a-qbanks/airbnb/raw/booking-system-design.txt) | Airbnb | Low | medium |
| [Notification System (Event + Batch)](../1p3a-qbanks/airbnb/raw/notification-system-design.txt) | Airbnb | Low | medium |
| [Pub-Sub Messaging System (OOD)](../1p3a-qbanks/amazon/raw/pub-sub-system-ood.txt) | Amazon | Low | medium |
| [Rate Limiter (OOD)](../1p3a-qbanks/amazon/raw/rate-limiter-ood.txt) | Amazon | Low | medium |
| [ML Configuration System](../1p3a-qbanks/anthropic/raw/ml-configuration-system.txt) | Anthropic | Low | medium |
| [System Design Q5 — Data Infrastructure](../1p3a-qbanks/anthropic/raw/sd-q5-data-infrastructure.txt) | Anthropic | Low | medium |
| [Ad Click Aggregator](../1p3a-qbanks/apple/raw/ad-click-aggregator.txt) | Apple | Low | medium |
| [Distributed Rate Limiter with Lua Details](../1p3a-qbanks/apple/raw/distributed-rate-limiter-with-lua.txt) | Apple | Low | medium |
| [Siri Grounded Response Generation](../1p3a-qbanks/apple/raw/siri-grounded-response-generation.txt) | Apple | Low | medium |
| [Product Feed and Shopping Recommendation System](../1p3a-qbanks/atlassian/raw/product-feed-and-shopping-recommendation-system.txt) | Atlassian | Low | medium |
| [Role and Resource Access System](../1p3a-qbanks/atlassian/raw/role-and-resource-access-system.txt) | Atlassian | Low | medium |
| [Tagging System REST API](../1p3a-qbanks/atlassian/raw/tagging-system-rest-api.txt) | Atlassian | Low | medium |
| [Short-Video Recommendation System](../1p3a-qbanks/bytedance/raw/short-video-recommendation-system.txt) | ByteDance / TikTok | Low | hard |
| [Video-Keyword Association Review System](../1p3a-qbanks/bytedance/raw/video-keyword-association-review-system.txt) | ByteDance / TikTok | Low | medium |
| [Frontend System Design — Crypto Trading UI Architecture](../1p3a-qbanks/coinbase/raw/fe-sd-trading-ui.txt) | Coinbase | Low | medium |
| [System Design — Crypto Brokerage / Trading Platform](../1p3a-qbanks/coinbase/raw/sd-crypto-brokerage.txt) | Coinbase | Low | medium |
| [Collaborative Playlist Design](../1p3a-qbanks/databricks/raw/collaborative-playlist-design.txt) | Databricks | Low | medium |
| [Harmful Content Detection ML Design](../1p3a-qbanks/databricks/raw/harmful-content-detection-ml-design.txt) | Databricks | Low | medium |
| [Async Job Scheduler](../1p3a-qbanks/figma/raw/async-job-scheduler-design.txt) | Figma | Low | – |
| [ML Model Design — Prompt-to-Design Generation](../1p3a-qbanks/figma/raw/prompt-to-design-generation.txt) | Figma | Low | – |
| [ML System Design — Asset / Template Recommendation & Feed](../1p3a-qbanks/figma/raw/ml-recsys-assets-feed.txt) | Figma | Low | – |
| [Template & Instance System with Update Propagation](../1p3a-qbanks/figma/raw/template-instance-propagation-system.txt) | Figma | Low | – |
| [System Design: Inventory Management](../1p3a-qbanks/instacart/raw/system-design-inventory-management.txt) | Instacart | Low | – |
| [System Design: Product Catalog](../1p3a-qbanks/instacart/raw/system-design-product-catalog.txt) | Instacart | Low | – |
| [Premium Product Recommendation System](../1p3a-qbanks/intuit/raw/premium-product-recommendation-system.txt) | Intuit | Low | medium |
| [Shopping Cart Latency and Inventory](../1p3a-qbanks/jpmorgan/raw/shopping-cart-latency-and-inventory.txt) | JPMorgan Chase | Low | medium |
| [LinkedIn Learning / Job Recommendation Ranking](../1p3a-qbanks/linkedin/raw/sd-job-recommendation-ranking.txt) | LinkedIn | Low | medium |
| [LinkedIn Skills — Data Mining & ML System Design](../1p3a-qbanks/linkedin/raw/sd-linkedin-skills-extraction.txt) | LinkedIn | Low | medium |
| [Single-Machine Key-Value Store with Filesystem Spill](../1p3a-qbanks/linkedin/raw/sd-single-machine-kv-store.txt) | LinkedIn | Low | medium |
| [Type-Ahead / Autocomplete Suggestions](../1p3a-qbanks/linkedin/raw/sd-typeahead-autocomplete.txt) | LinkedIn | Low | medium |
| [Charity Donation System](../1p3a-qbanks/lyft/raw/charity-donation-system.txt) | Lyft | Low | medium |
| [MLSD — Image Copyright Violation Detection](../1p3a-qbanks/meta/raw/mlsd-image-copyright-detection.txt) | Meta | Low | hard |
| [MLSD — Notification Ranking (Multi-source)](../1p3a-qbanks/meta/raw/mlsd-notification-ranking.txt) | Meta | Low | hard |
| [System Design — LeetCode / Online Judge + Contest Leaderboard](../1p3a-qbanks/meta/raw/system-design-leetcode-online-judge.txt) | Meta | Low | medium |
| [System Design — Ticketmaster / Ticketing](../1p3a-qbanks/meta/raw/system-design-ticketmaster.txt) | Meta | Low | medium |
| [System Design — Web Crawler / Google Flight](../1p3a-qbanks/meta/raw/system-design-web-crawler.txt) | Meta | Low | medium |
| [Ticket Booking / Ticketmaster System Design](../1p3a-qbanks/microsoft/raw/ticket-booking-system-design.txt) | Microsoft | Low | medium |
| [URL Shortener / Tiny URL System Design](../1p3a-qbanks/microsoft/raw/url-shortener-system-design.txt) | Microsoft | Low | medium |
| [Compute Resource Scheduling for ML Jobs](../1p3a-qbanks/netflix/raw/ml-compute-resource-scheduling.txt) | Netflix | Low | hard |
| [Design Google Calendar](../1p3a-qbanks/openai/raw/design-google-calendar.txt) | OpenAI | Low | – |
| [Design a Crossword Puzzle Solver](../1p3a-qbanks/openai/raw/crossword-puzzle-solver.txt) | OpenAI | Low | – |
| [Design a URL Shortener](../1p3a-qbanks/openai/raw/design-url-shortener.txt) | OpenAI | Low | – |
| [Design an AI Chatbot System](../1p3a-qbanks/openai/raw/design-ai-chatbot-system.txt) | OpenAI | Low | – |
| [Image Sharing with Content Deduplication](../1p3a-qbanks/openai/raw/image-sharing-content-deduplication.txt) | OpenAI | Low | medium |
| [Multi-Tenant CI/CD Workflow System](../1p3a-qbanks/openai/raw/multi-tenant-ci-cd-workflow.txt) | OpenAI | Low | – |
| [Points of Interest (POI) System / Yelp](../1p3a-qbanks/openai/raw/points-of-interest-yelp.txt) | OpenAI | Low | – |
| [RAG / Search ML Design (oral)](../1p3a-qbanks/openai/raw/rag-search-ml-design.txt) | OpenAI | Low | – |
| [Slack](../1p3a-qbanks/openai/raw/design-slack.txt) | OpenAI | Low | – |
| [Webhook Delivery System](../1p3a-qbanks/openai/raw/webhook-delivery-system.txt) | OpenAI | Low | – |
| [System Design — Rate Limiter](../1p3a-qbanks/oracle/raw/system-design-rate-limiter.txt) | Oracle | Low | medium |
| [System Design — VM Health Monitoring at 1M Scale](../1p3a-qbanks/oracle/raw/system-design-vm-health-monitoring.txt) | Oracle | Low | medium |
| [Durable Long-Running Agentic Query Execution](../1p3a-qbanks/perplexity/raw/durable-long-running-agentic-query-execution.txt) | Perplexity | Low | hard |
| [ML System Design: Bad / Unsafe Content Detection](../1p3a-qbanks/pinterest/raw/ml-system-design-content-safety.txt) | Pinterest | Low | hard |
| [System Design: Ads Event Aggregation / Reporting](../1p3a-qbanks/pinterest/raw/system-design-ads-event-aggregation.txt) | Pinterest | Low | medium |
| [System Design: Bulk + Single Catalog Update](../1p3a-qbanks/pinterest/raw/system-design-catalog-update-bulk-single.txt) | Pinterest | Low | medium |
| [System Design: Comment Notification Service](../1p3a-qbanks/pinterest/raw/system-design-comment-notifications-viral-pins.txt) | Pinterest | Low | medium |
| [System Design: Distributed Rate Limiter](../1p3a-qbanks/pinterest/raw/system-design-rate-limiter.txt) | Pinterest | Low | medium |
| [System Design: Leaderboard / Category Ranking](../1p3a-qbanks/pinterest/raw/system-design-leaderboard.txt) | Pinterest | Low | medium |
| [Game Leaderboard System Design](../1p3a-qbanks/reddit/raw/game-leaderboard-sd.txt) | Reddit | Low | medium |
| [ML Feature Store](../1p3a-qbanks/reddit/raw/feature-store-sd.txt) | Reddit | Low | medium |
| [Notification System](../1p3a-qbanks/reddit/raw/notification-system-sd.txt) | Reddit | Low | medium |
| [Post / Comment Ranking System](../1p3a-qbanks/reddit/raw/post-comment-ranking-sd.txt) | Reddit | Low | medium |
| [Subreddit Live Chat System](../1p3a-qbanks/reddit/raw/live-chat-system-sd.txt) | Reddit | Low | medium |
| [Photo Album Frontend System Design](../1p3a-qbanks/robinhood/raw/photo-album-frontend-design.txt) | Robinhood | Low | – |
| [Near-Real-Time Friend-Played-Game Counter](../1p3a-qbanks/roblox/raw/friend-played-game-near-realtime.txt) | Roblox | Low | hard |
| [To-Do List with Multi-User Collaboration](../1p3a-qbanks/roblox/raw/todo-list-multi-user-collab.txt) | Roblox | Low | medium |
| [Design an Analytics Metrics Dashboard for ChatGPT / LLM Service](../1p3a-qbanks/salesforce/raw/analytics-dashboard-llm-product.txt) | Salesforce | Low | medium |
| [Snap Ads Ranking](../1p3a-qbanks/snapchat/raw/snap-ads-ranking.txt) | Snapchat | Low | hard |
| [Design a Feature Flag System](../1p3a-qbanks/stripe/raw/feature-flag-system-design.txt) | Stripe | Low | medium |
| [Metric Counter Library](../1p3a-qbanks/stripe/raw/metric-counter-library.txt) | Stripe | Low | – |
| [Design Messenger](../1p3a-qbanks/uber/raw/messenger-design.txt) | Uber | Low | – |
| [Onsite System Design: Rider-Driver Matching](../1p3a-qbanks/uber/raw/onsite-sd-rider-driver-matching.txt) | Uber | Low | medium |
| [Uber Eats Search](../1p3a-qbanks/uber/raw/uber-eats-search.txt) | Uber | Low | – |
| [RAG Q&A Chatbot — ML / AI Technical Deep Dive](../1p3a-qbanks/vanta/raw/rag-chatbot-ml-design.txt) | Vanta | Low | – |
| [Design State-Wide Temperature Sensor Ingestion](../1p3a-qbanks/walmartlabs/raw/design-sensor-temperature-ingestion.txt) | Walmart Labs | Low | – |
| [ML System Design: Behavior Prediction from Sensor + Camera Data](../1p3a-qbanks/waymo/raw/sd-ml-behavior-prediction-from-sensors.txt) | Waymo | Low | hard |
| [Rate Limiter — Inbound + Outbound](../1p3a-qbanks/xai/raw/rate-limiter-design.txt) | xAI | Low | medium |
| [Payment Processing for Installment Loans](../1p3a-qbanks/affirm/raw/installment-loan-payment-processing.txt) | Affirm | Single report | medium |
| [Venmo-Style P2P Payment Data Model](../1p3a-qbanks/affirm/raw/venmo-payment-data-model.txt) | Affirm | Single report | – |
| [Airbnb Experiences — Search Ranking (ML Design)](../1p3a-qbanks/airbnb/raw/experiences-search-ranking-ml-design.txt) | Airbnb | Single report | medium |
| [Home Page — Search + Availability + Ranking](../1p3a-qbanks/airbnb/raw/home-page-search-availability-design.txt) | Airbnb | Single report | hard |
| [Host Listings Page — Aggregation Optimization](../1p3a-qbanks/airbnb/raw/host-listings-page-optimization.txt) | Airbnb | Single report | medium |
| [Improve Booking via Cover Photo Selection (ML Design)](../1p3a-qbanks/airbnb/raw/cover-photo-selection-ml-design.txt) | Airbnb | Single report | medium |
| [Listing Lifetime Value — Estimation (ML Design)](../1p3a-qbanks/airbnb/raw/listing-lifetime-value-ml-design.txt) | Airbnb | Single report | medium |
| [Query System — Time + Geo Filtered User Activity](../1p3a-qbanks/airbnb/raw/query-system-time-geo-search.txt) | Airbnb | Single report | medium |
| [Support Ticket Routing & Claiming System](../1p3a-qbanks/airbnb/raw/ticket-routing-system-design.txt) | Airbnb | Single report | medium |
| [Waitlist System Design](../1p3a-qbanks/airbnb/raw/waitlist-system-design.txt) | Airbnb | Single report | medium |
| [Core Dump System Design](../1p3a-qbanks/amazon/raw/core-dump-system-design.txt) | Amazon | Single report | medium |
| [Credit Card System (OOD)](../1p3a-qbanks/amazon/raw/credit-card-system-ood.txt) | Amazon | Single report | medium |
| [Distributed Training Data Pipeline (FAR)](../1p3a-qbanks/amazon/raw/distributed-training-data-pipeline.txt) | Amazon | Single report | hard |
| [ML System Design — Search / Ranking / Experimentation](../1p3a-qbanks/amazon/raw/as-ml-system-design-search-ranking.txt) | Amazon | Single report | hard |
| [Minesweeper Game Design](../1p3a-qbanks/amazon/raw/minesweeper-game-design.txt) | Amazon | Single report | medium |
| [Perishable-Goods Tracking & Location System](../1p3a-qbanks/amazon/raw/perishable-goods-tracking-system.txt) | Amazon | Single report | medium |
| [Permission Control System Design](../1p3a-qbanks/amazon/raw/permission-control-system.txt) | Amazon | Single report | medium |
| [Playlist / Guide Voting System (OOD)](../1p3a-qbanks/amazon/raw/playlist-guide-voting-ood.txt) | Amazon | Single report | medium |
| [Restaurant Waitlist & Table Assignment](../1p3a-qbanks/amazon/raw/restaurant-waitlist-table-assignment.txt) | Amazon | Single report | medium |
| [Ride-Hailing System Design (Uber)](../1p3a-qbanks/amazon/raw/ride-hailing-system-design.txt) | Amazon | Single report | – |
| [Apple News Search (No ML)](../1p3a-qbanks/apple/raw/apple-news-search-no-ml.txt) | Apple | Single report | – |
| [Cross-Device Wallpaper Synchronization](../1p3a-qbanks/apple/raw/cross-device-wallpaper-synchronization.txt) | Apple | Single report | medium |
| [Design (key, value) Storage with Snapshots](../1p3a-qbanks/apple/raw/design-key-value-storage-with-snapshots.txt) | Apple | Single report | – |
| [Design App Store Search](../1p3a-qbanks/apple/raw/design-app-store-search.txt) | Apple | Single report | – |
| [Design ChatGPT](../1p3a-qbanks/apple/raw/design-chatgpt.txt) | Apple | Single report | – |
| [Design News Feed](../1p3a-qbanks/apple/raw/design-news-feed.txt) | Apple | Single report | – |
| [Design TTL Cache](../1p3a-qbanks/apple/raw/design-ttl-cache.txt) | Apple | Single report | – |
| [Design a Chat Application](../1p3a-qbanks/apple/raw/design-a-chat-application.txt) | Apple | Single report | – |
| [Design a Concurrent Image Upload System](../1p3a-qbanks/apple/raw/design-a-concurrent-image-upload-system.txt) | Apple | Single report | – |
| [Design a RAG-Based Chatbot System](../1p3a-qbanks/apple/raw/design-a-rag-based-chatbot-system.txt) | Apple | Single report | – |
| [Distributed Task Scheduler](../1p3a-qbanks/apple/raw/distributed-task-scheduler.txt) | Apple | Single report | medium |
| [Large-Scale Media Transfer to ML Compute](../1p3a-qbanks/apple/raw/large-scale-media-transfer-to-ml-compute.txt) | Apple | Single report | hard |
| [Log Processing System](../1p3a-qbanks/apple/raw/log-processing-system.txt) | Apple | Single report | – |
| [Holiday Service Latency Optimization](../1p3a-qbanks/bloomberg/raw/holiday-service-latency-optimization.txt) | Bloomberg | Single report | medium |
| [Real-time Market Data System](../1p3a-qbanks/bloomberg/raw/real-time-market-data-system.txt) | Bloomberg | Single report | hard |
| [Streaming Social-Media Mentions & Aggregation](../1p3a-qbanks/bloomberg/raw/streaming-social-media-mentions-aggregation.txt) | Bloomberg | Single report | hard |
| [Trade Processor with Subscribe / Unsubscribe](../1p3a-qbanks/bloomberg/raw/trade-processor-subscribe-unsubscribe.txt) | Bloomberg | Single report | hard |
| [VWAP Analytic Provider](../1p3a-qbanks/bloomberg/raw/vwap-analytic-provider.txt) | Bloomberg | Single report | hard |
| [AI-Driven QA Pipeline Workflow](../1p3a-qbanks/bytedance/raw/ai-driven-qa-pipeline-workflow.txt) | ByteDance / TikTok | Single report | hard |
| [Agent Tool-Use System Design (AML Volcano Engine)](../1p3a-qbanks/bytedance/raw/agent-tool-use-system-design.txt) | ByteDance / TikTok | Single report | hard |
| [Design a Stripe-like Payment Platform](../1p3a-qbanks/bytedance/raw/payment-platform-design.txt) | ByteDance / TikTok | Single report | hard |
| [ML System Design: Dynamic K in Retrieval Stage](../1p3a-qbanks/bytedance/raw/dynamic-k-retrieval-ml-system-design.txt) | ByteDance / TikTok | Single report | hard |
| [Realtime Auction Bidding System](../1p3a-qbanks/bytedance/raw/realtime-auction-bidding-system.txt) | ByteDance / TikTok | Single report | medium |
| [Recent Like Count and Top Posts in a Sliding Window](../1p3a-qbanks/bytedance/raw/recent-like-count-and-top-posts.txt) | ByteDance / TikTok | Single report | medium |
| [Top Songs by Country Leaderboard](../1p3a-qbanks/bytedance/raw/top-songs-by-country-leaderboard.txt) | ByteDance / TikTok | Single report | hard |
| [Account Balance & Cross-Region Event Platform](../1p3a-qbanks/capitalone/raw/account-balance-cross-region-events.txt) | Capital One | Single report | – |
| [Fleet Configuration and Deployment Control Plane](../1p3a-qbanks/citadel/raw/sre-configuration-deployment-control-plane.txt) | Citadel / Citadel Securities | Single report | – |
| [Metrics Collection & Alerting System](../1p3a-qbanks/citadel/raw/metrics-alerting-system-design.txt) | Citadel / Citadel Securities | Single report | hard |
| [Frontend System Design — Signup Form](../1p3a-qbanks/coinbase/raw/fe-sd-signup-form.txt) | Coinbase | Single report | medium |
| [System Design — Credit Approval Risk Engine](../1p3a-qbanks/coinbase/raw/sd-credit-approval-risk-engine.txt) | Coinbase | Single report | medium |
| [System Design — Crypto Ledger & Wallet Transaction History](../1p3a-qbanks/coinbase/raw/sd-crypto-ledger-wallet-history.txt) | Coinbase | Single report | medium |
| [Design a Network Throttling System](../1p3a-qbanks/databricks/raw/network-throttling-system.txt) | Databricks | Single report | medium |
| [Design an Ad Marketplace System](../1p3a-qbanks/databricks/raw/ad-marketplace-system.txt) | Databricks | Single report | medium |
| [Design an Autonomous Vehicle Ride-Hailing App (Waymo-like)](../1p3a-qbanks/databricks/raw/waymo-ride-hailing-app.txt) | Databricks | Single report | medium |
| [Distributed File System](../1p3a-qbanks/databricks/raw/distributed-file-system.txt) | Databricks | Single report | medium |
| [Job Scheduler With Dependencies](../1p3a-qbanks/databricks/raw/job-scheduler-with-dependencies.txt) | Databricks | Single report | medium |
| [Payment Gateway System](../1p3a-qbanks/databricks/raw/payment-gateway-system.txt) | Databricks | Single report | medium |
| [Stock Trading Agent Service](../1p3a-qbanks/databricks/raw/stock-trading-agent.txt) | Databricks | Single report | medium |
| [DE / AE Onsite: Data Modeling (Fitness App)](../1p3a-qbanks/doordash/raw/de-ae-data-modeling.txt) | DoorDash | Single report | medium |
| [System Design: Customer Review Page (EM)](../1p3a-qbanks/doordash/raw/system-design-customer-review-page-em.txt) | DoorDash | Single report | medium |
| [System Design: Real-Time Restaurant Leaderboard](../1p3a-qbanks/doordash/raw/system-design-realtime-restaurant-leaderboard.txt) | DoorDash | Single report | medium |
| [System Design: Realtime Monitoring System](../1p3a-qbanks/doordash/raw/system-design-realtime-monitoring.txt) | DoorDash | Single report | medium |
| [Permission-Aware Retrieval & Ranking for Figma Files](../1p3a-qbanks/figma/raw/permission-aware-file-retrieval-ranking.txt) | Figma | Single report | hard |
| [Large-Scale Interactive Map Visualization](../1p3a-qbanks/google/raw/interactive-map-100m-datapoints.txt) | Google | Single report | hard |
| [Web Crawler at Search Scale](../1p3a-qbanks/google/raw/web-crawler-at-search-scale.txt) | Google | Single report | hard |
| [Million-User Flash Sale System](../1p3a-qbanks/jpmorgan/raw/million-user-flash-sale-system.txt) | JPMorgan Chase | Single report | medium |
| [Property Rental Search System](../1p3a-qbanks/jpmorgan/raw/property-rental-search-system.txt) | JPMorgan Chase | Single report | medium |
| [News Feed / Timeline](../1p3a-qbanks/linkedin/raw/sd-news-feed.txt) | LinkedIn | Single report | medium |
| [Real-Time Collaborative Code Editor](../1p3a-qbanks/linkedin/raw/sd-collab-coderpad.txt) | LinkedIn | Single report | medium |
| [System Design — Trending Hashtags](../1p3a-qbanks/meta/raw/system-design-trending-hashtags.txt) | Meta | Single report | medium |
| [Chatbot Personalization / Memory System Design](../1p3a-qbanks/microsoft/raw/chatbot-personalization-memory-design.txt) | Microsoft | Single report | – |
| [Distributed Web Crawler System Design](../1p3a-qbanks/microsoft/raw/web-crawler-system-design.txt) | Microsoft | Single report | medium |
| [Job Scheduler / ETL Pipeline System Design](../1p3a-qbanks/microsoft/raw/job-scheduler-etl-pipeline.txt) | Microsoft | Single report | medium |
| [Search Autocomplete / Typeahead System Design](../1p3a-qbanks/microsoft/raw/search-autocomplete-typeahead.txt) | Microsoft | Single report | medium |
| [Todo List Full-Stack System Design](../1p3a-qbanks/microsoft/raw/todo-list-full-stack-system-design.txt) | Microsoft | Single report | medium |
| [HPC Host Control Plane and Job Coordination](../1p3a-qbanks/nvidia/raw/hpc-host-control-plane-and-job-coordination.txt) | NVIDIA | Single report | medium |
| [LLM SQL Chatbot Access Control and Query Safety](../1p3a-qbanks/nvidia/raw/llm-sql-chatbot-access-control-and-query-safety.txt) | NVIDIA | Single report | medium |
| [Rate Limiter Algorithm Design](../1p3a-qbanks/nvidia/raw/rate-limiter-algorithm-design.txt) | NVIDIA | Single report | medium |
| [Real-Time Fraud Detection System](../1p3a-qbanks/nvidia/raw/real-time-fraud-detection-system.txt) | NVIDIA | Single report | medium |
| [Telemetry Collector and GPU Utilization Dashboard](../1p3a-qbanks/nvidia/raw/telemetry-collector-and-gpu-utilization-dashboard.txt) | NVIDIA | Single report | medium |
| [Ads Audience Targeting / Custom Audience System](../1p3a-qbanks/netflix/raw/ads-audience-targeting-system.txt) | Netflix | Single report | hard |
| [File Backup System](../1p3a-qbanks/netflix/raw/file-backup-system.txt) | Netflix | Single report | hard |
| [Global Config Deployment System](../1p3a-qbanks/netflix/raw/global-config-deployment-system.txt) | Netflix | Single report | hard |
| [Homepage Video Recommendation System](../1p3a-qbanks/netflix/raw/homepage-video-recommendation-system.txt) | Netflix | Single report | medium |
| [MLP Portal Full-Stack Design](../1p3a-qbanks/netflix/raw/mlp-portal-fullstack-design.txt) | Netflix | Single report | hard |
| [Promotion Posting System Data Model](../1p3a-qbanks/netflix/raw/promotion-posting-data-modeling.txt) | Netflix | Single report | hard |
| [Social Media Sentiment Tracking](../1p3a-qbanks/netflix/raw/social-media-sentiment-tracking.txt) | Netflix | Single report | medium |
| [Spam Email Detection](../1p3a-qbanks/netflix/raw/spam-email-detection.txt) | Netflix | Single report | medium |
| [Subscription Billing System (300M Subscribers)](../1p3a-qbanks/netflix/raw/subscription-billing-system.txt) | Netflix | Single report | hard |
| [WAL Log Enrichment Pipeline](../1p3a-qbanks/netflix/raw/wal-log-enrichment-pipeline.txt) | Netflix | Single report | hard |
| [ChatGPT Enterprise (RAG ML System Design)](../1p3a-qbanks/openai/raw/chatgpt-enterprise-rag.txt) | OpenAI | Single report | – |
| [GPT-3 Playground - Full-Stack Architecture](../1p3a-qbanks/openai/raw/gpt-3-playground.txt) | OpenAI | Single report | – |
| [Mining Novel Data from Large Unlabeled Corpus](../1p3a-qbanks/openai/raw/mining-novel-data-unlabeled-corpus.txt) | OpenAI | Single report | – |
| [Real-Time AI Product Feature](../1p3a-qbanks/openai/raw/real-time-ai-product-feature.txt) | OpenAI | Single report | – |
| [Low-Latency Trading System Design](../1p3a-qbanks/optiver/raw/trading-system-design.txt) | Optiver | Single report | – |
| [System Design — Event Ingestion + Top-K Aggregation](../1p3a-qbanks/oracle/raw/system-design-event-ingestion-topk.txt) | Oracle | Single report | medium |
| [System Design — GPU Health and Repair](../1p3a-qbanks/oracle/raw/system-design-gpu-health-repair.txt) | Oracle | Single report | – |
| [System Design — Healthcare Data Ingestion Pipeline](../1p3a-qbanks/oracle/raw/system-design-healthcare-data-ingestion-pipeline.txt) | Oracle | Single report | medium |
| [System Design — Video Upload + Search with LLM](../1p3a-qbanks/oracle/raw/system-design-video-upload-search.txt) | Oracle | Single report | medium |
| [System Design: Personalized Chatbot Recommending Pins](../1p3a-qbanks/pinterest/raw/system-design-personalized-chat-recommend.txt) | Pinterest | Single report | hard |
| [Design an Expedia-Style Hotel Reservation System](../1p3a-qbanks/ramp/raw/hotel-reservation-system.txt) | Ramp | Single report | hard |
| [Comment-Prediction ML System (will_user_comment_on_posts)](../1p3a-qbanks/reddit/raw/comment-prediction-mlsd.txt) | Reddit | Single report | medium |
| [Video Recommendation](../1p3a-qbanks/reddit/raw/video-recommendation-sd.txt) | Reddit | Single report | – |
| [Frontend News Feed](../1p3a-qbanks/rippling/raw/frontend-news-feed.txt) | Rippling | Single report | medium |
| [Design Instagram](../1p3a-qbanks/roblox/raw/design-instagram.txt) | Roblox | Single report | – |
| [Design Roblox Release Deployment Workflow](../1p3a-qbanks/roblox/raw/design-roblox-release-deployment-workflow.txt) | Roblox | Single report | – |
| [Design Roblox Wallet](../1p3a-qbanks/roblox/raw/design-roblox-wallet.txt) | Roblox | Single report | – |
| [Design a Multi-Resource Resource Loader](../1p3a-qbanks/roblox/raw/design-a-multi-resource-resource-loader.txt) | Roblox | Single report | – |
| [Design a ROS-like Pub/Sub System](../1p3a-qbanks/roblox/raw/design-a-ros-like-pub-sub-system.txt) | Roblox | Single report | – |
| [Feed Status App](../1p3a-qbanks/roblox/raw/feed-status-app.txt) | Roblox | Single report | – |
| [Image Feed (Frontend System Design)](../1p3a-qbanks/roblox/raw/image-feed-frontend-design.txt) | Roblox | Single report | medium |
| [ML System Design: Game Genre Classification From Scratch](../1p3a-qbanks/roblox/raw/ml-system-design-game-genre-classification.txt) | Roblox | Single report | medium |
| [Notification System](../1p3a-qbanks/roblox/raw/notification-system.txt) | Roblox | Single report | – |
| [Online Fraud Detection System](../1p3a-qbanks/roblox/raw/online-fraud-detection-system.txt) | Roblox | Single report | – |
| [TinyURL (URL Shortener)](../1p3a-qbanks/roblox/raw/tiny-url.txt) | Roblox | Single report | medium |
| [Design a Collaborative Spreadsheet (Google Sheets)](../1p3a-qbanks/salesforce/raw/google-sheets-design.txt) | Salesforce | Single report | hard |
| [Design a High-Throughput Distributed Cache](../1p3a-qbanks/salesforce/raw/high-throughput-cache-design.txt) | Salesforce | Single report | hard |
| [Design an Async Job / Task System](../1p3a-qbanks/salesforce/raw/async-job-task-system.txt) | Salesforce | Single report | medium |
| [Design an Enterprise Messaging / Collaboration System](../1p3a-qbanks/salesforce/raw/enterprise-messaging-collaboration-system.txt) | Salesforce | Single report | medium |
| [Rate Limiter for Expensive API with User-Configurable Monthly Quota](../1p3a-qbanks/salesforce/raw/rate-limiter-user-configurable-quota.txt) | Salesforce | Single report | medium |
| [ACL Service for Another Service](../1p3a-qbanks/snowflake/raw/acl-service-design.txt) | Snowflake | Single report | medium |
| [Audit Logs Service](../1p3a-qbanks/snowflake/raw/audit-logs-service.txt) | Snowflake | Single report | medium |
| [Cross-Platform Logging Library](../1p3a-qbanks/snowflake/raw/logging-library-design.txt) | Snowflake | Single report | medium |
| [Design Distributed Metrics System](../1p3a-qbanks/snowflake/raw/design-distributed-metrics-system.txt) | Snowflake | Single report | medium |
| [Design a Quota System](../1p3a-qbanks/snowflake/raw/design-a-quota-system.txt) | Snowflake | Single report | medium |
| [Design an RPC Abstraction Layer](../1p3a-qbanks/snowflake/raw/rpc-abstraction-layer.txt) | Snowflake | Single report | medium |
| [Distributed Tree Node Count](../1p3a-qbanks/snowflake/raw/distributed-tree-node-count.txt) | Snowflake | Single report | hard |
| [Jira AI Auto-Fix System](../1p3a-qbanks/snowflake/raw/jira-ai-auto-fix-system.txt) | Snowflake | Single report | medium |
| [S3-Style Storage with Dedup](../1p3a-qbanks/snowflake/raw/s3-dedup-storage-design.txt) | Snowflake | Single report | medium |
| [Web URL Crawler at Scale](../1p3a-qbanks/snowflake/raw/web-url-crawler-at-scale.txt) | Snowflake | Single report | medium |
| [Account Takeover Prediction System](../1p3a-qbanks/stripe/raw/account-takeover-prediction-design.txt) | Stripe | Single report | – |
| [Fraud Detection System](../1p3a-qbanks/stripe/raw/fraud-detection-system-design.txt) | Stripe | Single report | – |
| [Payment Webhook System](../1p3a-qbanks/stripe/raw/payment-webhook-system.txt) | Stripe | Single report | – |
| [Ticketmaster-Style Seat Booking Design](../1p3a-qbanks/tesla/raw/ticketmaster-double-booking-design.txt) | Tesla | Single report | medium |
| [Design a Pickup Area Driver Queue](../1p3a-qbanks/uber/raw/pickup-area-driver-queue-design.txt) | Uber | Single report | – |
| [Design a Stock Price Alert Notification System](../1p3a-qbanks/uber/raw/stock-price-alert-design.txt) | Uber | Single report | – |
| [Design an Online Shopping Cart](../1p3a-qbanks/uber/raw/online-shopping-cart-design.txt) | Uber | Single report | – |
| [Fraud Detection](../1p3a-qbanks/uber/raw/fraud-detection-design.txt) | Uber | Single report | – |
| [Onsite System Design: App User Behavior Data Platform](../1p3a-qbanks/uber/raw/onsite-sd-app-user-behavior-data-platform.txt) | Uber | Single report | medium |
| [Onsite System Design: Daily Puzzle / Submission Scoring](../1p3a-qbanks/uber/raw/onsite-sd-puzzle-scoring.txt) | Uber | Single report | medium |
| [Onsite System Design: Nearby Driver Proximity Service](../1p3a-qbanks/uber/raw/onsite-sd-nearby-driver-proximity-service.txt) | Uber | Single report | hard |
| [Design Multi-Carrier Package Delivery Routing System](../1p3a-qbanks/walmartlabs/raw/design-multi-carrier-delivery-system.txt) | Walmart Labs | Single report | – |
| [Design Ticketmaster — Event Booking & Seat Reservation](../1p3a-qbanks/walmartlabs/raw/design-ticketmaster-event-booking.txt) | Walmart Labs | Single report | – |
| [Design Walmart+ Membership System](../1p3a-qbanks/walmartlabs/raw/design-membership-system.txt) | Walmart Labs | Single report | – |
| [ML System Design — Predict Item Category](../1p3a-qbanks/walmartlabs/raw/mlsd-predict-item-category.txt) | Walmart Labs | Single report | – |
| [Design a Global Cache for Small Images](../1p3a-qbanks/waymo/raw/global-small-image-cache.txt) | Waymo | Single report | hard |
| [ML System Design: Choose Passenger Drop-off Location](../1p3a-qbanks/waymo/raw/sd-passenger-dropoff-location-ml.txt) | Waymo | Single report | hard |
| [ML System Design: Inference Serving with Back-of-Envelope Capacity Planning](../1p3a-qbanks/waymo/raw/sd-ml-inference-serving-back-of-envelope.txt) | Waymo | Single report | hard |
| [System Design: Elevator Operation Logic](../1p3a-qbanks/waymo/raw/sd-elevator-operation-logic.txt) | Waymo | Single report | medium |
| [System Design: Evaluate a Self-Driving Model End-to-End](../1p3a-qbanks/waymo/raw/sd-evaluate-self-driving-model.txt) | Waymo | Single report | hard |
| [System Design: Evaluation System with Human + LLM Evaluators](../1p3a-qbanks/waymo/raw/sd-evaluation-system-llm-human.txt) | Waymo | Single report | hard |
| [System Design: Parking Lot + Robotaxi Dispatch](../1p3a-qbanks/waymo/raw/sd-parking-and-dispatch.txt) | Waymo | Single report | hard |
| [System Design: Simulation Log Collection and Result Aggregation](../1p3a-qbanks/waymo/raw/sd-simulation-log-aggregation.txt) | Waymo | Single report | hard |
| [Design a RAG System](../1p3a-qbanks/xai/raw/rag-system-design.txt) | xAI | Single report | medium |
| [Notification System — Push to Followers on New Post](../1p3a-qbanks/xai/raw/notification-system-design.txt) | xAI | Single report | medium |

</details>

<details><summary><b>graph</b> (292)</summary>

| Question | Company | Freq | Level |
|---|---|---|---|
| [AI Coding — Maze Solver](../1p3a-qbanks/meta/raw/ai-coding-maze-solver.txt) | Meta | Very high | medium |
| [Infection Spread / Cellular Automata](../1p3a-qbanks/openai/raw/infection-spread-cellular-automata.txt) | OpenAI | Very high | medium |
| [Social Network / Follow Graph](../1p3a-qbanks/openai/raw/social-network-follow-graph.txt) | OpenAI | Very high | medium |
| [Service Shutdown / Topological Sort](../1p3a-qbanks/amazon/raw/service-shutdown-topological-sort.txt) | Amazon | High | medium |
| [Number of Islands and Grid BFS Variants](../1p3a-qbanks/bytedance/raw/number-of-islands-family.txt) | ByteDance / TikTok | High | medium |
| [Optimal Commute Grid](../1p3a-qbanks/databricks/raw/optimal-commute-grid.txt) | Databricks | High | medium |
| [Spreadsheet / Formula Engine](../1p3a-qbanks/harvey/raw/spreadsheet-formula-engine.txt) | Harvey | High | medium |
| [Topological Sort / Course Schedule for Ads](../1p3a-qbanks/netflix/raw/topological-sort-course-schedule-ads.txt) | Netflix | High | medium |
| [Service Load Factor / Referral Count](../1p3a-qbanks/robinhood/raw/service-load-factor-referral-count.txt) | Robinhood | High | medium |
| [Task Processor: Dependencies and Deadlines](../1p3a-qbanks/scale.ai/raw/task-processor-dependencies-and-deadlines.txt) | Scale AI | High | medium |
| [Closest Bathroom / Desk on a Grid](../1p3a-qbanks/snowflake/raw/closest-bathroom-desk-grid.txt) | Snowflake | High | medium |
| [Course Schedule with Time / Batches](../1p3a-qbanks/snowflake/raw/course-schedule-time-batches.txt) | Snowflake | High | medium |
| [Delivery-Center Grid — Minimum Inconvenience](../1p3a-qbanks/amazon/raw/delivery-center-grid-minimum-inconvenience.txt) | Amazon | Medium | hard |
| [Binary Tree Maximum Path Sum (with path reconstruction)](../1p3a-qbanks/bytedance/raw/binary-tree-max-path-sum.txt) | ByteDance / TikTok | Medium | hard |
| [Course Schedule and Topological Sort on a Directed Graph](../1p3a-qbanks/bytedance/raw/course-schedule-topo-sort.txt) | ByteDance / TikTok | Medium | medium |
| [Mining Block — Fee-Maximizing Transactions](../1p3a-qbanks/coinbase/raw/mining-block-knapsack.txt) | Coinbase | Medium | medium |
| [Generalized Tic-Tac-Toe](../1p3a-qbanks/databricks/raw/tic-tac-toe-generalized.txt) | Databricks | Medium | medium |
| [File / Folder / Team Permissions — Fewest Grants](../1p3a-qbanks/figma/raw/file-system-permissions.txt) | Figma | Medium | medium |
| [Friend Graph with Timeline (Friend / Unfriend Events)](../1p3a-qbanks/google/raw/friend-graph-with-timeline.txt) | Google | Medium | hard |
| [Trie Prefix Search / Autocomplete](../1p3a-qbanks/google/raw/trie-prefix-search-autocomplete.txt) | Google | Medium | medium |
| [Onsite Coding: Expression Evaluation with Variables](../1p3a-qbanks/instacart/raw/onsite-expression-evaluation-with-variables.txt) | Instacart | Medium | medium |
| [Rotten Oranges / Grid Infection BFS](../1p3a-qbanks/lyft/raw/rotten-oranges-grid-bfs.txt) | Lyft | Medium | medium |
| [AI Coding — Friend Recommendation](../1p3a-qbanks/meta/raw/ai-coding-friend-recommendation.txt) | Meta | Medium | medium |
| [Binary Tree Vertical Order / Right Side View](../1p3a-qbanks/meta/raw/binary-tree-vertical-order-right-side-view.txt) | Meta | Medium | medium |
| [DNA Shotgun Sequencing](../1p3a-qbanks/microsoft/raw/dna-shotgun-sequencing.txt) | Microsoft | Medium | hard |
| [Version Dependency](../1p3a-qbanks/openai/raw/version-dependency.txt) | OpenAI | Medium | medium |
| [ToDo List for AI](../1p3a-qbanks/perplexity/raw/todo-list-for-ai.txt) | Perplexity | Medium | hard |
| [Pin / Board Graph Distance](../1p3a-qbanks/pinterest/raw/pin-board-shortest-distance.txt) | Pinterest | Medium | medium |
| [Reconstruct Itinerary (LC 332)](../1p3a-qbanks/pinterest/raw/reconstruct-itinerary.txt) | Pinterest | Medium | medium |
| [URL Maze — Recursive API Crawl to "Congrats"](../1p3a-qbanks/ramp/raw/url-maze-crawler.txt) | Ramp | Medium | medium |
| [Topological Sort with Secondary Ordering](../1p3a-qbanks/roblox/raw/topological-sort-with-ordering.txt) | Roblox | Medium | medium |
| [DAG Allow / Disallow Propagation](../1p3a-qbanks/snowflake/raw/dag-allow-disallow-letters.txt) | Snowflake | Medium | medium |
| [Tree Node Deletion → Max Height](../1p3a-qbanks/snowflake/raw/tree-node-deletion-max-height.txt) | Snowflake | Medium | medium |
| [SWE / QSE OA — Sewer Tree Partition](../1p3a-qbanks/twosigma/raw/swe-oa-sewer-tree-partition.txt) | Two Sigma | Medium | medium |
| [OA: Splash Zone / Balls Connection](../1p3a-qbanks/uber/raw/oa-splash-zone-union-find.txt) | Uber | Medium | hard |
| [Phone Screen: Number of Islands (Plain and Streaming)](../1p3a-qbanks/uber/raw/phone-screen-number-of-islands.txt) | Uber | Medium | medium |
| [Phone Screen: Service Dependency Topological Order](../1p3a-qbanks/uber/raw/phone-screen-topological-dependency-order.txt) | Uber | Medium | medium |
| [Phone Screen: Word Search on Straight 8-Direction Lines](../1p3a-qbanks/uber/raw/phone-screen-word-search-straight-line.txt) | Uber | Medium | medium |
| [Employee Training Status + Group Tree Aggregation](../1p3a-qbanks/vanta/raw/training-status-group-aggregation.txt) | Vanta | Medium | medium |
| [Task Dependency Resolution (Topological Sort)](../1p3a-qbanks/vanta/raw/task-dependency-resolution.txt) | Vanta | Medium | medium |
| [Word Pattern / Meta-Pattern Match (Backtracking)](../1p3a-qbanks/vanta/raw/word-pattern-meta-match.txt) | Vanta | Medium | medium |
| [Account Merge / User Deduplication](../1p3a-qbanks/airbnb/raw/account-merge-dedup-users.txt) | Airbnb | Low | medium |
| [Board Score — Connected Areas × Crowns](../1p3a-qbanks/airbnb/raw/board-score-area-crowns.txt) | Airbnb | Low | medium |
| [Chain Booking — Max Cascading Reservations](../1p3a-qbanks/airbnb/raw/chain-booking-cascade.txt) | Airbnb | Low | medium |
| [Connect-4-Style Board Game](../1p3a-qbanks/airbnb/raw/board-game-ood.txt) | Airbnb | Low | easy |
| [Key Store With Sum-Dependency Cascades](../1p3a-qbanks/airbnb/raw/key-dependency-sum-store.txt) | Airbnb | Low | medium |
| [Ski Path — Max Score on a Weighted DAG](../1p3a-qbanks/airbnb/raw/ski-path-max-score-dag.txt) | Airbnb | Low | medium |
| [Terrain Rendering + Water Drop Simulation](../1p3a-qbanks/airbnb/raw/terrain-water-drop.txt) | Airbnb | Low | medium |
| [QR HackerRank: Profitable Pairs and Delivery Order](../1p3a-qbanks/akunacapital/raw/qr-hackerrank-profitable-pairs-and-delivery-order.txt) | Akuna Capital | Low | medium |
| [Count Connected Components (Union-Find)](../1p3a-qbanks/amazon/raw/count-connected-components-union-find.txt) | Amazon | Low | medium |
| [Minimum Replacements to Make Array Contiguous](../1p3a-qbanks/amazon/raw/min-ops-to-make-array-contiguous.txt) | Amazon | Low | hard |
| [Package Dependency Installation & Build Order](../1p3a-qbanks/amazon/raw/package-dependency-installation-build-order.txt) | Amazon | Low | medium |
| [Flatten a Multilevel Doubly Linked List](../1p3a-qbanks/bloomberg/raw/flatten-multilevel-doubly-linked-list.txt) | Bloomberg | Low | medium |
| [Shortest Path in Grid with K Wall Breaks](../1p3a-qbanks/bloomberg/raw/shortest-path-with-k-wall-breaks.txt) | Bloomberg | Low | hard |
| [Subway / Network Connectivity](../1p3a-qbanks/bloomberg/raw/subway-network-connectivity.txt) | Bloomberg | Low | medium |
| [Word Search in 2D Grid](../1p3a-qbanks/bloomberg/raw/word-search-grid.txt) | Bloomberg | Low | medium |
| [Alien Dictionary](../1p3a-qbanks/bytedance/raw/alien-dictionary.txt) | ByteDance / TikTok | Low | hard |
| [Restore IP Addresses](../1p3a-qbanks/bytedance/raw/restore-ip-addresses.txt) | ByteDance / TikTok | Low | medium |
| [Matrix Border Sort & Clockwise Fill](../1p3a-qbanks/capitalone/raw/matrix-border-sort-clockwise-fill.txt) | Capital One | Low | medium |
| [Matrix Commands: Reverse / Swap / Rotate](../1p3a-qbanks/capitalone/raw/matrix-commands-reverse-swap-rotate.txt) | Capital One | Low | easy |
| [Matrix Expression Max Value](../1p3a-qbanks/capitalone/raw/matrix-expression-max-value.txt) | Capital One | Low | hard |
| [Print All Root-to-Leaf Tree Paths](../1p3a-qbanks/capitalone/raw/print-tree-paths.txt) | Capital One | Low | easy |
| [Tetris-Style Figure Placement](../1p3a-qbanks/capitalone/raw/tetris-figure-placement.txt) | Capital One | Low | medium |
| [Generate Random NFT (DFS + Weighted Sampling)](../1p3a-qbanks/coinbase/raw/nft-generation-combinations.txt) | Coinbase | Low | medium |
| [Uniformly Connect Node Groups](../1p3a-qbanks/databricks/raw/randomly-connect-node-groups.txt) | Databricks | Low | hard |
| [Code Craft: Nearest Destination on 2D Grid (Multi-Source BFS)](../1p3a-qbanks/doordash/raw/code-craft-multi-source-bfs-grid.txt) | DoorDash | Low | medium |
| [Code Craft: Restaurant Delivery Heatmap](../1p3a-qbanks/doordash/raw/code-craft-restaurant-heatmap.txt) | DoorDash | Low | medium |
| [Largest Tree in a Forest](../1p3a-qbanks/goldmansachs/raw/largest-tree-in-forest.txt) | Goldman Sachs | Low | medium |
| [2D Points Connected Components by Radius](../1p3a-qbanks/google/raw/points-connected-components-by-radius.txt) | Google | Low | medium |
| [Compute File System Total Size](../1p3a-qbanks/google/raw/file-system-total-size.txt) | Google | Low | medium |
| [Count Same-Color Squares in Matrix](../1p3a-qbanks/google/raw/count-same-color-squares.txt) | Google | Low | medium |
| [Expression Tree Single-Leaf Mutation](../1p3a-qbanks/google/raw/expression-tree-single-leaf-mutation.txt) | Google | Low | medium |
| [Matrix Flower Placement with House Adjacency](../1p3a-qbanks/google/raw/matrix-flower-placement-with-house-adjacency.txt) | Google | Low | hard |
| [Number of Islands](../1p3a-qbanks/google/raw/number-of-islands.txt) | Google | Low | medium |
| [Path Minimizing the Maximum Cell (Swim in Rising Water)](../1p3a-qbanks/google/raw/path-min-max-grid-dijkstra.txt) | Google | Low | medium |
| [Rotten Oranges / Multi-Source BFS (taxis)](../1p3a-qbanks/google/raw/rotten-oranges-multi-source-bfs-taxis.txt) | Google | Low | medium |
| [Earliest Coordinate Reach with Optional Moves](../1p3a-qbanks/ibm/raw/earliest-coordinate-reach-with-optional-moves.txt) | IBM | Low | medium |
| [Onsite Coding: File Matrix Lookup](../1p3a-qbanks/instacart/raw/onsite-file-matrix-lookup.txt) | Instacart | Low | medium |
| [Alien Dictionary](../1p3a-qbanks/meta/raw/alien-dictionary.txt) | Meta | Low | hard |
| [Mouse and Cheese / Maze Rewards](../1p3a-qbanks/meta/raw/mouse-and-cheese-maze-rewards.txt) | Meta | Low | medium |
| [Range Sum of BST](../1p3a-qbanks/meta/raw/range-sum-of-bst.txt) | Meta | Low | easy |
| [Tree Diameter / Longest Path](../1p3a-qbanks/meta/raw/tree-diameter.txt) | Meta | Low | medium |
| [Walls and Gates / Max Area of Island](../1p3a-qbanks/meta/raw/walls-and-gates-multi-source-bfs.txt) | Meta | Low | medium |
| [Minimum Fuel Cost Across a Road Network](../1p3a-qbanks/microsoft/raw/minimum-fuel-cost-road-network.txt) | Microsoft | Low | hard |
| [2-D Matrix Transpose with Memory-Layout Discussion](../1p3a-qbanks/nvidia/raw/matrix-transpose-memory-layout.txt) | NVIDIA | Low | medium |
| [Currency Arbitrage Detection](../1p3a-qbanks/optiver/raw/currency-arbitrage-detection.txt) | Optiver | Low | medium |
| [Satellite Message Propagation](../1p3a-qbanks/optiver/raw/satellite-message-propagation.txt) | Optiver | Low | hard |
| [Maximum Completable Tasks with Prerequisites (Topological)](../1p3a-qbanks/oracle/raw/course-prerequisites-task-count.txt) | Oracle | Low | medium |
| [Number of Islands (LC 200)](../1p3a-qbanks/oracle/raw/number-of-islands.txt) | Oracle | Low | medium |
| [Robot Vacuum 8-Direction Grid](../1p3a-qbanks/pinterest/raw/robot-vacuum-grid.txt) | Pinterest | Low | medium |
| [Tree-Encoded Subtree Deletion and Stable Compaction](../1p3a-qbanks/pinterest/raw/tree-encoded-subtree-deletion-compaction.txt) | Pinterest | Low | medium |
| [Maximum Safe Steps on a Laser Grid](../1p3a-qbanks/roblox/raw/laser-grid-maximum-safe-steps.txt) | Roblox | Low | medium |
| [Flatten Nested JSON / HashMap to String](../1p3a-qbanks/salesforce/raw/flatten-nested-json.txt) | Salesforce | Low | medium |
| [Neuron Firing Cellular Automata](../1p3a-qbanks/scale.ai/raw/neuron-firing-cellular-automata.txt) | Scale AI | Low | medium |
| [Course Schedule Cycle Detection](../1p3a-qbanks/snapchat/raw/course-schedule-cycle-detection.txt) | Snapchat | Low | medium |
| [Escape Grid with Fire and Waiting Time](../1p3a-qbanks/snapchat/raw/escape-grid-with-fire-and-waiting-time.txt) | Snapchat | Low | hard |
| [Maximum Island Perimeter](../1p3a-qbanks/snapchat/raw/maximum-island-perimeter.txt) | Snapchat | Low | medium |
| [Rewrite Tree With Subtree Sums](../1p3a-qbanks/snowflake/raw/rewrite-tree-with-subtree-sums.txt) | Snowflake | Low | medium |
| [Wiki Page Shortest-Click Path](../1p3a-qbanks/snowflake/raw/wiki-page-shortest-path.txt) | Snowflake | Low | medium |
| [Perfect Squares — Return the Shortest Decomposition](../1p3a-qbanks/pinduoduo/raw/perfect-squares-shortest-path.txt) | Temu / Pinduoduo | Low | medium |
| [Spiral Matrix Generation](../1p3a-qbanks/pinduoduo/raw/spiral-matrix-generation.txt) | Temu / Pinduoduo | Low | medium |
| [Word Ladder Variants with Trie Optimization](../1p3a-qbanks/tesla/raw/word-ladder-variants.txt) | Tesla | Low | hard |
| [Construct Quad Tree](../1p3a-qbanks/uber/raw/construct-quad-tree.txt) | Uber | Low | medium |
| [Number of Islands II (Dynamic Union-Find)](../1p3a-qbanks/uber/raw/number-of-islands-ii.txt) | Uber | Low | medium |
| [OA: Minimum Edge Reversals from Every Root](../1p3a-qbanks/uber/raw/oa-min-edge-reversals.txt) | Uber | Low | hard |
| [Phone Screen: Alien Dictionary (LC 269)](../1p3a-qbanks/uber/raw/phone-screen-alien-dictionary.txt) | Uber | Low | hard |
| [Phone Screen: Bus Routes (LC 815)](../1p3a-qbanks/uber/raw/phone-screen-bus-routes.txt) | Uber | Low | hard |
| [Randomly Populate Grid with Connected Equal-Size Token Regions](../1p3a-qbanks/waymo/raw/random-grid-token-fill.txt) | Waymo | Low | hard |
| [Shortest Distance from All Buildings](../1p3a-qbanks/waymo/raw/shortest-distance-from-all-buildings.txt) | Waymo | Low | hard |
| [Boxes, Keys & Candies (OOD)](../1p3a-qbanks/airbnb/raw/boxes-keys-candies-ood.txt) | Airbnb | Single report | medium |
| [Maximum Difference Across Connected Components](../1p3a-qbanks/akunacapital/raw/maximum-difference-connected-components.txt) | Akuna Capital | Single report | easy |
| [Domain Score Accumulation (Reverse Trie)](../1p3a-qbanks/amazon/raw/domain-score-trie.txt) | Amazon | Single report | medium |
| [Fulfillment-Center Inventory Transfer](../1p3a-qbanks/amazon/raw/fulfillment-center-inventory-transfer.txt) | Amazon | Single report | medium |
| [Print Hierarchical Comments](../1p3a-qbanks/amazon/raw/hierarchical-comments-print.txt) | Amazon | Single report | medium |
| [Spreadsheet Cell Relationships](../1p3a-qbanks/amazon/raw/spreadsheet-cell-relationships.txt) | Amazon | Single report | medium |
| [Tree Node Relationship — Sibling / Cousin / Other](../1p3a-qbanks/amazon/raw/tree-node-relationship-sibling-cousin.txt) | Amazon | Single report | easy |
| [Word Search II](../1p3a-qbanks/amazon/raw/word-search-ii.txt) | Amazon | Single report | – |
| [Alias Group Mapping](../1p3a-qbanks/apple/raw/alias-group-mapping.txt) | Apple | Single report | medium |
| [All Nodes Distance K in Binary Tree (LC 863)](../1p3a-qbanks/apple/raw/all-nodes-distance-k-in-binary-tree.txt) | Apple | Single report | medium |
| [Binary Tree Vertical Order Traversal](../1p3a-qbanks/apple/raw/binary-tree-vertical-order-traversal.txt) | Apple | Single report | medium |
| [Bus Route Earliest Arrival](../1p3a-qbanks/apple/raw/bus-route-earliest-arrival.txt) | Apple | Single report | hard |
| [Cheapest Flights Within K Stops](../1p3a-qbanks/apple/raw/cheapest-flights-within-k-stops.txt) | Apple | Single report | hard |
| [Course Schedule](../1p3a-qbanks/apple/raw/course-schedule.txt) | Apple | Single report | medium |
| [Flood Fill](../1p3a-qbanks/apple/raw/flood-fill.txt) | Apple | Single report | easy |
| [Kubernetes Service Filter & Dependency Chain](../1p3a-qbanks/apple/raw/kubernetes-service-filter-and-dependency-chain.txt) | Apple | Single report | medium |
| [Number of Islands](../1p3a-qbanks/apple/raw/number-of-islands.txt) | Apple | Single report | medium |
| [Number of Islands II](../1p3a-qbanks/apple/raw/number-of-islands-ii.txt) | Apple | Single report | medium |
| [Out of Boundary Paths](../1p3a-qbanks/apple/raw/out-of-boundary-paths.txt) | Apple | Single report | hard |
| [Reconstruct Itinerary](../1p3a-qbanks/apple/raw/reconstruct-itinerary.txt) | Apple | Single report | medium |
| [Redundant Connection](../1p3a-qbanks/apple/raw/redundant-connection.txt) | Apple | Single report | medium |
| [Rotate Image](../1p3a-qbanks/apple/raw/rotate-image.txt) | Apple | Single report | medium |
| [Search a 2D Matrix](../1p3a-qbanks/apple/raw/search-a-2d-matrix.txt) | Apple | Single report | medium |
| [Serialize and Deserialize Binary Tree](../1p3a-qbanks/apple/raw/serialize-and-deserialize-binary-tree.txt) | Apple | Single report | hard |
| [Shortest Bridge (Distance Between Two Islands)](../1p3a-qbanks/apple/raw/shortest-bridge-distance-between-two-islands.txt) | Apple | Single report | medium |
| [Shortest Path in a Grid with Obstacle Removal](../1p3a-qbanks/apple/raw/shortest-path-in-grid-with-obstacle-removal.txt) | Apple | Single report | medium |
| [Sparse Matrix Multiplication (LC 311)](../1p3a-qbanks/apple/raw/sparse-matrix-multiplication.txt) | Apple | Single report | medium |
| [Valid Sudoku](../1p3a-qbanks/apple/raw/valid-sudoku.txt) | Apple | Single report | easy |
| [Cart Routes Origin Destinations](../1p3a-qbanks/atlassian/raw/cart-routes-origin-destinations.txt) | Atlassian | Single report | medium |
| [Karat Carpool on Linear Routes](../1p3a-qbanks/atlassian/raw/karat-carpool-linear-routes.txt) | Atlassian | Single report | medium |
| [Binary Tree Vertical Order Traversal](../1p3a-qbanks/bloomberg/raw/binary-tree-vertical-order-traversal.txt) | Bloomberg | Single report | medium |
| [Find Tree Root from Edge List](../1p3a-qbanks/bloomberg/raw/find-tree-root-from-edges.txt) | Bloomberg | Single report | easy |
| [Gas Tank Grid Reachability](../1p3a-qbanks/bloomberg/raw/gas-tank-grid-bfs.txt) | Bloomberg | Single report | hard |
| [Validate Binary Search Tree](../1p3a-qbanks/bloomberg/raw/validate-binary-search-tree.txt) | Bloomberg | Single report | medium |
| [AI-Assisted Order Validator](../1p3a-qbanks/bytedance/raw/ai-assisted-order-validator.txt) | ByteDance / TikTok | Single report | medium |
| [Binary Tree Right Side View](../1p3a-qbanks/bytedance/raw/binary-tree-right-side-view.txt) | ByteDance / TikTok | Single report | medium |
| [Equalize Root-to-Leaf Path Sums in an N-ary Tree](../1p3a-qbanks/bytedance/raw/equalize-root-to-leaf-path-sums.txt) | ByteDance / TikTok | Single report | medium |
| [Flatten a Multilevel Doubly Linked List (Skip Empty Nodes)](../1p3a-qbanks/bytedance/raw/flatten-multilevel-list-skip-empty-nodes.txt) | ByteDance / TikTok | Single report | medium |
| [Grid Shortest Path with K Obstacle Eliminations](../1p3a-qbanks/bytedance/raw/grid-shortest-path-with-obstacle-elimination.txt) | ByteDance / TikTok | Single report | medium |
| [Longest Zigzag Path in a Grid](../1p3a-qbanks/bytedance/raw/longest-zigzag-path-grid.txt) | ByteDance / TikTok | Single report | medium |
| [Minimum-Cost Path on a Grid with Fuel and Recharge Cells](../1p3a-qbanks/bytedance/raw/shortest-path-grid-with-fuel.txt) | ByteDance / TikTok | Single report | hard |
| [N-ary Tree Path Sum Count](../1p3a-qbanks/bytedance/raw/n-ary-tree-path-sum-count.txt) | ByteDance / TikTok | Single report | medium |
| [Photo Groups via Transitive Similarity](../1p3a-qbanks/bytedance/raw/friend-circles-photo-groups.txt) | ByteDance / TikTok | Single report | medium |
| [Reconstruct Binary Tree from Preorder and Postorder](../1p3a-qbanks/bytedance/raw/reconstruct-binary-tree-from-preorder-and-postorder.txt) | ByteDance / TikTok | Single report | medium |
| [Valid Parentheses with Wildcard '*' and DFS All Strings](../1p3a-qbanks/bytedance/raw/valid-parentheses-with-wildcard.txt) | ByteDance / TikTok | Single report | medium |
| [Word Search II with Trie](../1p3a-qbanks/bytedance/raw/word-search-ii-trie.txt) | ByteDance / TikTok | Single report | hard |
| [Color Match-Three Grid](../1p3a-qbanks/capitalone/raw/color-match-three-grid.txt) | Capital One | Single report | medium |
| [Laser Grid Robot Max Safe Run](../1p3a-qbanks/capitalone/raw/laser-grid-robot-max-safe-run.txt) | Capital One | Single report | medium |
| [Matrix Zig-Zag Traversal](../1p3a-qbanks/cisco/raw/matrix-zig-zag-traversal.txt) | Cisco | Single report | medium |
| [Row Maximum / Column Minimum](../1p3a-qbanks/cisco/raw/row-maximum-column-minimum.txt) | Cisco | Single report | easy |
| [Word Search in Rows and Columns](../1p3a-qbanks/cisco/raw/word-search-rows-columns.txt) | Cisco | Single report | easy |
| [2048 Simulation + Compress State to a `long long`](../1p3a-qbanks/citadel/raw/simulate-2048-encode-state.txt) | Citadel / Citadel Securities | Single report | medium |
| [Game of Life — In-Place and Infinite Board](../1p3a-qbanks/citadel/raw/game-of-life-in-place-infinite.txt) | Citadel / Citadel Securities | Single report | medium |
| [N-ary Tree Sum + Leaf `next` Pointer](../1p3a-qbanks/citadel/raw/tree-sum-leaf-next-pointer.txt) | Citadel / Citadel Securities | Single report | medium |
| [Task Dependency Ordering](../1p3a-qbanks/citadel/raw/task-dependency-ordering.txt) | Citadel / Citadel Securities | Single report | medium |
| [Maximal Square and Rectangle in Binary Matrix](../1p3a-qbanks/databricks/raw/maximal-square-and-rectangle.txt) | Databricks | Single report | medium |
| [Encyclopedia Removal Grid](../1p3a-qbanks/goldmansachs/raw/encyclopedia-removal-grid.txt) | Goldman Sachs | Single report | medium |
| [Ethernet Cable Square Count](../1p3a-qbanks/goldmansachs/raw/ethernet-cable-square-count.txt) | Goldman Sachs | Single report | easy |
| [Maximum-Sum Path in a Matrix (No Revisits)](../1p3a-qbanks/goldmansachs/raw/matrix-max-sum-path-no-repeat.txt) | Goldman Sachs | Single report | hard |
| [Role Permission Inheritance and Overrides](../1p3a-qbanks/goldmansachs/raw/role-permission-inheritance-overrides.txt) | Goldman Sachs | Single report | medium |
| [Spiral Matrix Traversal](../1p3a-qbanks/goldmansachs/raw/spiral-matrix-output.txt) | Goldman Sachs | Single report | easy |
| [Longest Consecutive Path in Binary Tree](../1p3a-qbanks/google/raw/binary-tree-longest-consecutive-path.txt) | Google | Single report | medium |
| [Longest Same-Color Grid Sequence](../1p3a-qbanks/google/raw/longest-same-color-grid-line.txt) | Google | Single report | – |
| [Minimum Direction Violations in a Directed Graph](../1p3a-qbanks/google/raw/minimum-direction-violations-directed-graph.txt) | Google | Single report | medium |
| [OA: Feature Rollout with Dependency Cycle Detection](../1p3a-qbanks/google/raw/oa-feature-rollout-dependency-cycle-report.txt) | Google | Single report | hard |
| [Rotate Any m×n 2D Matrix](../1p3a-qbanks/google/raw/rotate-matrix-rectangle.txt) | Google | Single report | medium |
| [Shortest Directed Cycle Through a Node](../1p3a-qbanks/google/raw/shortest-cycle-through-node.txt) | Google | Single report | medium |
| [Template Variable Expansion with Follow-Ups](../1p3a-qbanks/google/raw/template-variable-expansion-dependencies.txt) | Google | Single report | medium |
| [Tic-Tac-Toe System with K Players and 3-in-a-Row Win](../1p3a-qbanks/google/raw/tic-tac-toe-k-players-three-in-a-row.txt) | Google | Single report | medium |
| [Tree Distance Sum (Re-root DP)](../1p3a-qbanks/google/raw/tree-distance-sum-reroot.txt) | Google | Single report | hard |
| [Duplicate File Discovery](../1p3a-qbanks/harvey/raw/duplicate-file-discovery.txt) | Harvey | Single report | medium |
| [Minimum Link Reallocation to Connect Repositories](../1p3a-qbanks/ibm/raw/minimum-link-reallocation.txt) | IBM | Single report | medium |
| [Small Business Network: Degrees of Separation](../1p3a-qbanks/intuit/raw/small-business-network-degrees-of-separation.txt) | Intuit | Single report | medium |
| [Three-Column Grid Coloring Count](../1p3a-qbanks/intuit/raw/three-column-grid-coloring-count.txt) | Intuit | Single report | medium |
| [Prerequisite Cycle OA](../1p3a-qbanks/jpmorgan/raw/prerequisite-cycle-oa.txt) | JPMorgan Chase | Single report | medium |
| [Robot Navigation in an Unknown Grid](../1p3a-qbanks/linkedin/raw/coding-robot-navigation-unknown-grid.txt) | LinkedIn | Single report | medium |
| [Service Dependency Impact Propagation](../1p3a-qbanks/linkedin/raw/coding-service-dependency-impact.txt) | LinkedIn | Single report | medium |
| [Minesweeper Game Logic](../1p3a-qbanks/lyft/raw/minesweeper-game-logic.txt) | Lyft | Single report | medium |
| [Maximum K-Star Sum](../1p3a-qbanks/mathworks/raw/maximum-k-star-sum.txt) | MathWorks | Single report | medium |
| [Number of Islands API Variant](../1p3a-qbanks/meta/raw/number-of-islands-api-variant.txt) | Meta | Single report | medium |
| [Shortest Path in a Binary Matrix (LC 1091)](../1p3a-qbanks/meta/raw/shortest-path-binary-matrix.txt) | Meta | Single report | medium |
| [Sum Root-to-Leaf Numbers](../1p3a-qbanks/meta/raw/sum-root-to-leaf-numbers.txt) | Meta | Single report | medium |
| [15-Puzzle Minimum Moves (BFS)](../1p3a-qbanks/microsoft/raw/15-puzzle-bfs.txt) | Microsoft | Single report | medium |
| [Alien Dictionary (LC 269)](../1p3a-qbanks/microsoft/raw/alien-dictionary.txt) | Microsoft | Single report | medium |
| [Binary Tree Zigzag Level Order Traversal (LC 103)](../1p3a-qbanks/microsoft/raw/binary-tree-zigzag-level-order.txt) | Microsoft | Single report | medium |
| [Distance from Each Node to the Cycle in an Undirected Graph](../1p3a-qbanks/microsoft/raw/distance-to-cycle-in-undirected-graph.txt) | Microsoft | Single report | hard |
| [In-Place Sub-Matrix Move](../1p3a-qbanks/microsoft/raw/in-place-submatrix-move.txt) | Microsoft | Single report | medium |
| [Lowest Common Ancestor (LC 235) with a Follow-Up Variant](../1p3a-qbanks/microsoft/raw/lowest-common-ancestor-bst-binary-tree.txt) | Microsoft | Single report | medium |
| [Spiral Matrix (LC 54) + Follow-Ups](../1p3a-qbanks/microsoft/raw/spiral-matrix-traversal.txt) | Microsoft | Single report | medium |
| [Computation / Dependency Graph Validation and Pruning](../1p3a-qbanks/nvidia/raw/graph-validation-and-pruning.txt) | NVIDIA | Single report | medium |
| [Reconstruct Itinerary](../1p3a-qbanks/netflix/raw/reconstruct-itinerary.txt) | Netflix | Single report | hard |
| [Dependency Cycle Detection (Deadlock)](../1p3a-qbanks/okta/raw/dependency-cycle-detection.txt) | Okta | Single report | medium |
| [Maximum-Score Grid Path with Limited Jumps](../1p3a-qbanks/openai/raw/maximum-score-grid-path-limited-jumps.txt) | OpenAI | Single report | hard |
| [OpenSheet: Spreadsheet with Cell Dependencies](../1p3a-qbanks/openai/raw/opensheet-spreadsheet.txt) | OpenAI | Single report | medium |
| [Accounts Merge (LC 721)](../1p3a-qbanks/oracle/raw/accounts-merge-lc721.txt) | Oracle | Single report | medium |
| [Course Scheduler — Print Path (Simplified)](../1p3a-qbanks/oracle/raw/course-scheduler-simplified.txt) | Oracle | Single report | easy |
| [Set Matrix Zeros (in-place)](../1p3a-qbanks/oracle/raw/set-matrix-zeros-inplace.txt) | Oracle | Single report | medium |
| [Spiral Matrix (LC 54)](../1p3a-qbanks/oracle/raw/spiral-matrix-lc54.txt) | Oracle | Single report | medium |
| [Trie-Based Autocomplete](../1p3a-qbanks/oracle/raw/trie-autocomplete.txt) | Oracle | Single report | medium |
| [Premium-Zone Escape Path](../1p3a-qbanks/perplexity/raw/premium-zone-escape-path.txt) | Perplexity | Single report | medium |
| [Graph Distance / Connectivity (MLE Phone Variant)](../1p3a-qbanks/pinterest/raw/graph-distance-mle-screen.txt) | Pinterest | Single report | medium |
| [Lighthouse Light Propagation](../1p3a-qbanks/pinterest/raw/lighthouse-2d-matrix.txt) | Pinterest | Single report | medium |
| [Excel Cell Calculation](../1p3a-qbanks/ramp/raw/excel-cell-calculation.txt) | Ramp | Single report | hard |
| [Word Ladder (LC 127)](../1p3a-qbanks/reddit/raw/word-ladder.txt) | Reddit | Single report | – |
| [Word Ladder with One- or Two-Character Moves](../1p3a-qbanks/reddit/raw/word-ladder-two-char-moves.txt) | Reddit | Single report | medium |
| [Word Search](../1p3a-qbanks/reddit/raw/word-search.txt) | Reddit | Single report | medium |
| [Word Search II](../1p3a-qbanks/reddit/raw/word-search-ii.txt) | Reddit | Single report | hard |
| [Currency Exchange / Maximize After Two Days of Conversions](../1p3a-qbanks/rippling/raw/currency-exchange.txt) | Rippling | Single report | medium |
| [Intern OA Fixed Set](../1p3a-qbanks/rippling/raw/intern-oa-fixed-set.txt) | Rippling | Single report | medium |
| [Candy Crush Grid Matching and Gravity](../1p3a-qbanks/roblox/raw/candy-crush-grid-matching-and-gravity.txt) | Roblox | Single report | medium |
| [Grid Pathfinding with Obstacles (DFS)](../1p3a-qbanks/roblox/raw/dfs-grid-with-obstacles.txt) | Roblox | Single report | easy |
| [Find the Celebrity](../1p3a-qbanks/salesforce/raw/find-the-celebrity.txt) | Salesforce | Single report | medium |
| [Maximal Rectangle (LeetCode 85)](../1p3a-qbanks/salesforce/raw/maximal-rectangle.txt) | Salesforce | Single report | hard |
| [Maximal Square (LeetCode 221)](../1p3a-qbanks/salesforce/raw/maximal-square.txt) | Salesforce | Single report | medium |
| [Minimum One-Character Word Transformations](../1p3a-qbanks/salesforce/raw/minimum-word-transformation-steps.txt) | Salesforce | Single report | medium |
| [Max Area of Island (LC 695)](../1p3a-qbanks/snapchat/raw/max-area-of-island-lc-695.txt) | Snapchat | Single report | medium |
| [Most Stones Removed with Same Row or Column (LC 947)](../1p3a-qbanks/snapchat/raw/most-stones-removed-with-same-row-or-column.txt) | Snapchat | Single report | medium |
| [Service Dependency Shortest Latency Path](../1p3a-qbanks/snapchat/raw/service-dependency-shortest-latency-path.txt) | Snapchat | Single report | medium |
| [Word Ladder Reachability](../1p3a-qbanks/snapchat/raw/word-ladder-reachability.txt) | Snapchat | Single report | medium |
| [01 Matrix](../1p3a-qbanks/snowflake/raw/01-matrix.txt) | Snowflake | Single report | medium |
| [Boundary of Binary Tree](../1p3a-qbanks/snowflake/raw/boundary-of-binary-tree.txt) | Snowflake | Single report | medium |
| [Cheapest Flights Within K Stops](../1p3a-qbanks/snowflake/raw/cheapest-flights-within-k-stops.txt) | Snowflake | Single report | medium |
| [Frontend Grid: Robot Eats Candies](../1p3a-qbanks/snowflake/raw/frontend-grid-robot-candy.txt) | Snowflake | Single report | medium |
| [Grid Drop and Remove Duplicates](../1p3a-qbanks/snowflake/raw/grid-drop-and-remove-duplicates.txt) | Snowflake | Single report | medium |
| [Longest Univalue Path](../1p3a-qbanks/snowflake/raw/longest-univalue-path.txt) | Snowflake | Single report | medium |
| [Pre-order Traversal Skipping Marked Nodes](../1p3a-qbanks/snowflake/raw/tree-preorder-skip-invalid.txt) | Snowflake | Single report | easy |
| [Reach Target via Add / Double / Halve](../1p3a-qbanks/snowflake/raw/reach-target-add-double-halve.txt) | Snowflake | Single report | medium |
| [Service Failure Forensics](../1p3a-qbanks/snowflake/raw/service-failure-forensics.txt) | Snowflake | Single report | medium |
| [Step-By-Step Directions From a Binary Tree Node to Another](../1p3a-qbanks/snowflake/raw/binary-tree-step-by-step-directions.txt) | Snowflake | Single report | medium |
| [Word Search](../1p3a-qbanks/snowflake/raw/word-search.txt) | Snowflake | Single report | medium |
| [Word Search II](../1p3a-qbanks/snowflake/raw/word-search-ii.txt) | Snowflake | Single report | hard |
| [Choice Options to Ending Rooms](../1p3a-qbanks/sofi/raw/choice-options-to-ending-rooms.txt) | SoFi | Single report | medium |
| [Permutations with Self-Written Tests](../1p3a-qbanks/sofi/raw/permutations-with-self-written-tests.txt) | SoFi | Single report | medium |
| [Reachable Nodes in a Directed Graph](../1p3a-qbanks/sofi/raw/reachable-nodes-in-directed-graph.txt) | SoFi | Single report | medium |
| [Rooms with Two Incoming Edges to Treasure](../1p3a-qbanks/sofi/raw/rooms-with-two-incoming-edges-to-treasure.txt) | SoFi | Single report | easy |
| [Currency Exchange Rate Converter](../1p3a-qbanks/stripe/raw/currency-exchange-rate-converter.txt) | Stripe | Single report | hard |
| [Factory Cost — Min-Cost Path Across Layers](../1p3a-qbanks/stripe/raw/factory-cost-min-path.txt) | Stripe | Single report | medium |
| [Record Linkage by Weighted Similarity](../1p3a-qbanks/stripe/raw/record-linkage-similarity.txt) | Stripe | Single report | medium |
| [Six Degrees of Collusion (OA)](../1p3a-qbanks/stripe/raw/six-degrees-of-collusion-oa.txt) | Stripe | Single report | medium |
| [Minesweeper (Variant)](../1p3a-qbanks/pinduoduo/raw/minesweeper-variant.txt) | Temu / Pinduoduo | Single report | medium |
| [Validate Binary Search Tree (LC 98)](../1p3a-qbanks/pinduoduo/raw/validate-binary-search-tree.txt) | Temu / Pinduoduo | Single report | medium |
| [Debug Dijkstra Shortest Path for Navigation](../1p3a-qbanks/tesla/raw/dijkstra-code-review-navigation.txt) | Tesla | Single report | medium |
| [First Solar Panel Placement in a Binary Grid](../1p3a-qbanks/tesla/raw/solar-panel-placement-grid.txt) | Tesla | Single report | medium |
| [Rank Vehicles for OTA Peer-to-Peer Seeding](../1p3a-qbanks/tesla/raw/ota-p2p-vehicle-seeding-rank.txt) | Tesla | Single report | medium |
| [Robot Room Navigation Take-Home](../1p3a-qbanks/tesla/raw/robot-room-navigation-takehome.txt) | Tesla | Single report | medium |
| [Maximum Independent Set on a Tree](../1p3a-qbanks/twosigma/raw/maximum-independent-set-on-tree.txt) | Two Sigma | Single report | hard |
| [Maximum Product Path in a Complete Directed Graph](../1p3a-qbanks/twosigma/raw/maximum-product-path-in-complete-directed-graph.txt) | Two Sigma | Single report | hard |
| [Binary Tree Longest Consecutive Sequence II](../1p3a-qbanks/uber/raw/bt-longest-consecutive-sequence-ii.txt) | Uber | Single report | medium |
| [Cheapest Flights Within K Stops](../1p3a-qbanks/uber/raw/cheapest-flights-within-k-stops.txt) | Uber | Single report | medium |
| [Checking Existence of Edge Length Limited Paths](../1p3a-qbanks/uber/raw/edge-length-limited-paths.txt) | Uber | Single report | medium |
| [Count Paths That Can Form a Palindrome in a Tree](../1p3a-qbanks/uber/raw/count-palindrome-paths-tree.txt) | Uber | Single report | hard |
| [Earliest Time to Connect All Riders](../1p3a-qbanks/uber/raw/earliest-time-connect-all-riders.txt) | Uber | Single report | medium |
| [Evaluate Division](../1p3a-qbanks/uber/raw/evaluate-division.txt) | Uber | Single report | medium |
| [Fill Dashes with Nearest Letter](../1p3a-qbanks/uber/raw/matrix-fill-nearest-letter.txt) | Uber | Single report | medium |
| [Leftmost Column with At Least a One](../1p3a-qbanks/uber/raw/leftmost-column-with-a-one.txt) | Uber | Single report | medium |
| [Lowest Common Ancestor of a Binary Tree](../1p3a-qbanks/uber/raw/lowest-common-ancestor-bt.txt) | Uber | Single report | medium |
| [Making a Large Island](../1p3a-qbanks/uber/raw/making-a-large-island.txt) | Uber | Single report | hard |
| [Maximum Number of Points from Grid Queries](../1p3a-qbanks/uber/raw/max-points-grid-queries.txt) | Uber | Single report | hard |
| [Minimum Knight Moves](../1p3a-qbanks/uber/raw/minimum-knight-moves.txt) | Uber | Single report | medium |
| [N-ary Tree Level Order Traversal](../1p3a-qbanks/uber/raw/nary-tree-level-order-traversal.txt) | Uber | Single report | medium |
| [Onsite Coding: Minesweeper (LC 529)](../1p3a-qbanks/uber/raw/onsite-coding-minesweeper-lc-529.txt) | Uber | Single report | medium |
| [Palindrome Ancestor Path Queries](../1p3a-qbanks/uber/raw/palindrome-ancestor-path-queries.txt) | Uber | Single report | hard |
| [Phone Screen OOD: Filesystem Navigation](../1p3a-qbanks/uber/raw/phone-screen-ood-filesystem-navigation.txt) | Uber | Single report | hard |
| [Rotate Image In Place](../1p3a-qbanks/uber/raw/rotate-image-in-place.txt) | Uber | Single report | medium |
| [Rotting Oranges](../1p3a-qbanks/uber/raw/rotting-oranges.txt) | Uber | Single report | medium |
| [Serialize / Deserialize Binary Tree with N-ary Follow-up](../1p3a-qbanks/uber/raw/serialize-deserialize-binary-tree-nary-followup.txt) | Uber | Single report | medium |
| [Shortest Bridge](../1p3a-qbanks/uber/raw/shortest-bridge.txt) | Uber | Single report | medium |
| [Vertical Order Traversal of a Binary Tree](../1p3a-qbanks/uber/raw/vertical-order-traversal-bt.txt) | Uber | Single report | hard |
| [Word Search](../1p3a-qbanks/uber/raw/word-search.txt) | Uber | Single report | medium |
| [Word Search II](../1p3a-qbanks/uber/raw/word-search-ii.txt) | Uber | Single report | hard |
| [Course Schedule II (LC 210)](../1p3a-qbanks/walmartlabs/raw/course-schedule-ii-lc-210.txt) | Walmart Labs | Single report | medium |
| [Validate Nonogram Solution](../1p3a-qbanks/walmartlabs/raw/validate-nonogram.txt) | Walmart Labs | Single report | medium |
| [Validate N×N Grid as Latin Square](../1p3a-qbanks/walmartlabs/raw/validate-square-grid.txt) | Walmart Labs | Single report | medium |
| [Car Maze with Incrementally Revealed Neighbors (DFS)](../1p3a-qbanks/waymo/raw/car-maze-incremental-discovery-dfs.txt) | Waymo | Single report | easy |
| [Chess Piece Shortest Path on a Fixed Board](../1p3a-qbanks/waymo/raw/chess-shortest-path-fixed-board.txt) | Waymo | Single report | medium |
| [Count Same-Color Squares in an Unbounded Grid](../1p3a-qbanks/waymo/raw/same-color-squares-in-grid.txt) | Waymo | Single report | medium |
| [Implement Max Pooling with Argmax Coordinates](../1p3a-qbanks/waymo/raw/max-pooling-with-argmax-coordinates.txt) | Waymo | Single report | medium |
| [Prefix Autocomplete via Trie](../1p3a-qbanks/waymo/raw/trie-prefix-autocomplete.txt) | Waymo | Single report | medium |
| [Race Car: Minimum Instructions to Reach a Target](../1p3a-qbanks/waymo/raw/race-car-minimum-instructions.txt) | Waymo | Single report | hard |
| [Rectangle Copy Inside a 2-D Array with O(1) Memory](../1p3a-qbanks/waymo/raw/rectangle-copy-o1-memory.txt) | Waymo | Single report | medium |
| [Shortest Path from Source to Target Nodes (Dijkstra)](../1p3a-qbanks/waymo/raw/dijkstra-shortest-path-to-targets.txt) | Waymo | Single report | medium |
| [Universal Direction Sequence for an Unknown-Position Robot in a Maze](../1p3a-qbanks/waymo/raw/robot-maze-universal-direction-sequence.txt) | Waymo | Single report | hard |
| [Validate a Forest from (parent, child) Edges](../1p3a-qbanks/waymo/raw/forest-validation-parent-child-edges.txt) | Waymo | Single report | medium |

</details>

<details><summary><b>stateful-oop-design</b> (154)</summary>

| Question | Company | Freq | Level |
|---|---|---|---|
| [Coding Q2 — File Deduplication](../1p3a-qbanks/anthropic/raw/coding-q2-file-deduplication.txt) | Anthropic | Very high | medium |
| [Debugging: Pick Dasher / Round Robin Load Balancer](../1p3a-qbanks/doordash/raw/debugging-pick-dasher-round-robin.txt) | DoorDash | Very high | medium |
| [Coding Q2 — In-Memory Cache: Bug-Fix + Durability](../1p3a-qbanks/anthropic/raw/coding-q2-lru-cache-durability.txt) | Anthropic | High | medium |
| [Transaction Filter + Pagination Endpoint](../1p3a-qbanks/coinbase/raw/transaction-filter-pagination.txt) | Coinbase | High | medium |
| [Code Craft: Bootstrap Aggregated API](../1p3a-qbanks/doordash/raw/code-craft-bootstrap-aggregated-api.txt) | DoorDash | High | medium |
| [Transactional In-Memory Key-Value Store](../1p3a-qbanks/lyft/raw/transactional-in-memory-kv-store.txt) | Lyft | High | hard |
| [CodeSignal OA — Banking System (Progressive OOD)](../1p3a-qbanks/meta/raw/codesignal-oa-banking-system.txt) | Meta | High | hard |
| [GPU Credits / GPU Credit 2](../1p3a-qbanks/openai/raw/gpu-credits.txt) | OpenAI | High | medium |
| [Memory Allocator](../1p3a-qbanks/openai/raw/memory-allocator.txt) | OpenAI | High | hard |
| [Monster Battle System](../1p3a-qbanks/openai/raw/monster-battle-system.txt) | OpenAI | High | medium |
| [Toy Language / Type Inference](../1p3a-qbanks/openai/raw/toy-language-type-inference.txt) | OpenAI | High | medium |
| [Tennis Match Scoring](../1p3a-qbanks/reddit/raw/tennis-match-scoring.txt) | Reddit | High | medium |
| [Expense Rules Engine](../1p3a-qbanks/rippling/raw/expense-rules-engine.txt) | Rippling | High | hard |
| [Mars Rover / Robot Controller (CLI)](../1p3a-qbanks/shopify/raw/mars-rover-robot-controller.txt) | Shopify | High | medium |
| [In-Memory File System OA](../1p3a-qbanks/airbnb/raw/oa-in-memory-file-system.txt) | Airbnb | Medium | medium |
| [Communications Handler (CommsHandler)](../1p3a-qbanks/akunacapital/raw/communications-handler.txt) | Akuna Capital | Medium | medium |
| [LRU Cache (LC 146)](../1p3a-qbanks/amazon/raw/lru-cache.txt) | Amazon | Medium | medium |
| [Coding & Design — Weighted Data Batcher with Checkpointing](../1p3a-qbanks/anthropic/raw/coding-design-data-batcher.txt) | Anthropic | Medium | hard |
| [OA — Bank System with Transfer/Accept and Merge](../1p3a-qbanks/anthropic/raw/oa-bank-system.txt) | Anthropic | Medium | medium |
| [OA — In-Memory Database (4-Level CodeSignal)](../1p3a-qbanks/anthropic/raw/oa-in-memory-database.txt) | Anthropic | Medium | medium |
| [OA — Recipe Manager (4-Level CodeSignal)](../1p3a-qbanks/anthropic/raw/oa-recipe-manager.txt) | Anthropic | Medium | medium |
| [Bank Class OOD: Deposit / Withdraw / Transfer](../1p3a-qbanks/capitalone/raw/bank-class-ood-transactions.txt) | Capital One | Medium | medium |
| [Virtual Credit Card Validator (Case Study)](../1p3a-qbanks/capitalone/raw/virtual-credit-card-validator.txt) | Capital One | Medium | medium |
| [Crypto Order Management (Kafka Consumer)](../1p3a-qbanks/coinbase/raw/crypto-order-management-kafka.txt) | Coinbase | Medium | medium |
| [In-Memory Database / Cloud Storage (Multi-Level OA)](../1p3a-qbanks/coinbase/raw/in-memory-database-oa.txt) | Coinbase | Medium | medium |
| [Lazy Array](../1p3a-qbanks/databricks/raw/lazy-array.txt) | Databricks | Medium | medium |
| [RLE + Bit-Packing Encoder / Decoder](../1p3a-qbanks/databricks/raw/rle-bit-packing-encoder-decoder.txt) | Databricks | Medium | medium |
| [Snapshot Set / MVCC Iterator](../1p3a-qbanks/databricks/raw/snapshot-set-iterator.txt) | Databricks | Medium | medium |
| [FigJam Table — Selection & Set Color](../1p3a-qbanks/figma/raw/figjam-table-selection-set-color.txt) | Figma | Medium | medium |
| [In-Memory File System](../1p3a-qbanks/harvey/raw/in-memory-file-system.txt) | Harvey | Medium | medium |
| [Karat AI: Bus Simulation Codebase](../1p3a-qbanks/instacart/raw/karat-ai-bus-simulation-codebase.txt) | Instacart | Medium | – |
| [OA: In-Memory Key-Value Store](../1p3a-qbanks/instacart/raw/oa-in-memory-key-value-store.txt) | Instacart | Medium | hard |
| [Pagination API Wrapper](../1p3a-qbanks/lyft/raw/pagination-api-wrapper.txt) | Lyft | Medium | medium |
| [In-Memory SQL Engine](../1p3a-qbanks/microsoft/raw/in-memory-sql-engine.txt) | Microsoft | Medium | hard |
| [Object-Oriented Design Round](../1p3a-qbanks/microsoft/raw/object-oriented-design-round.txt) | Microsoft | Medium | medium |
| [Homepage Viewport / Shelf Dedupe](../1p3a-qbanks/netflix/raw/homepage-viewport-shelf-dedupe.txt) | Netflix | Medium | medium |
| [Chat Bot System Refactoring](../1p3a-qbanks/openai/raw/chat-bot-system-refactoring.txt) | OpenAI | Medium | medium |
| [Durable Key-Value Store Serialization](../1p3a-qbanks/openai/raw/durable-kv-store-serialization.txt) | OpenAI | Medium | medium |
| [Hot Air Balloon Festival Simulation](../1p3a-qbanks/optiver/raw/balloon-festival-simulation.txt) | Optiver | Medium | medium |
| [Squirrel Nut Storage Tracker](../1p3a-qbanks/optiver/raw/squirrel-nut-storage.txt) | Optiver | Medium | hard |
| [Splitwise / Settle Group Balances](../1p3a-qbanks/pinterest/raw/splitwise-settle-balances.txt) | Pinterest | Medium | medium |
| [Camel Cards / Poker OOD](../1p3a-qbanks/rippling/raw/camel-cards-poker-ood.txt) | Rippling | Medium | medium |
| [Logger OOD](../1p3a-qbanks/rippling/raw/logger-ood.txt) | Rippling | Medium | medium |
| [Web API Round: Stack Overflow / Quora-like Service](../1p3a-qbanks/rippling/raw/web-api-stack-overflow-quora.txt) | Rippling | Medium | medium |
| [URL Shortener Service (CLI)](../1p3a-qbanks/shopify/raw/url-shortener-service.txt) | Shopify | Medium | medium |
| [Onsite OOD / Coding: Uber Eats Cart & Pricing Engine](../1p3a-qbanks/uber/raw/onsite-ood-eats-cart-pricing-engine.txt) | Uber | Medium | hard |
| [Onsite OOD: Multi-Level Parking Lot](../1p3a-qbanks/uber/raw/onsite-ood-parking-lot.txt) | Uber | Medium | medium |
| [Phone Screen: First Customer Who Visited Only Once](../1p3a-qbanks/uber/raw/phone-screen-first-unique-visitor.txt) | Uber | Medium | medium |
| [Phone Screen: Rate Limiter (Coding + Trade-offs)](../1p3a-qbanks/uber/raw/phone-screen-rate-limiter.txt) | Uber | Medium | medium |
| [Twitter Insight Platform (CodeSignal Take-Home)](../1p3a-qbanks/xai/raw/twitter-insight-platform-take-home.txt) | xAI | Medium | medium |
| [C++ Object Pool Debugging](../1p3a-qbanks/akunacapital/raw/c-plus-plus-object-pool-debugging.txt) | Akuna Capital | Low | medium |
| [Earliest One-Time Visiting User](../1p3a-qbanks/amazon/raw/earliest-one-time-visiting-user.txt) | Amazon | Low | medium |
| [Out-of-Order Package Receiver](../1p3a-qbanks/amazon/raw/out-of-order-package-receiver.txt) | Amazon | Low | medium |
| [OA — File Systems](../1p3a-qbanks/anthropic/raw/oa-file-systems.txt) | Anthropic | Low | medium |
| [OA — Task Manager (TTL + Snapshot)](../1p3a-qbanks/anthropic/raw/oa-task-management.txt) | Anthropic | Low | medium |
| [Lottery System / Insert Delete GetRandom O(1)](../1p3a-qbanks/bloomberg/raw/lottery-system-insert-delete-getrandom.txt) | Bloomberg | Low | medium |
| [Order Book Pair Coding](../1p3a-qbanks/citadel/raw/order-book-pair-coding.txt) | Citadel / Citadel Securities | Low | medium |
| [Revenue Tracking System with Referrals](../1p3a-qbanks/databricks/raw/revenue-tracking-with-referrals.txt) | Databricks | Low | medium |
| [Frontend Autocomplete Component](../1p3a-qbanks/lyft/raw/frontend-autocomplete-component.txt) | Lyft | Low | medium |
| [MOOC Course Purchase OOD](../1p3a-qbanks/mathworks/raw/mooc-course-purchase-ood.txt) | MathWorks | Low | medium |
| [CodeSignal OA — In-Memory Database](../1p3a-qbanks/meta/raw/codesignal-oa-in-memory-database.txt) | Meta | Low | – |
| [Resumable Batch Dataloader with Weighted Sampling](../1p3a-qbanks/microsoft/raw/resumable-batch-dataloader.txt) | Microsoft | Low | hard |
| [Disk Space Manager / KV Store with setAll](../1p3a-qbanks/nvidia/raw/storage-manager-and-kv-setall.txt) | NVIDIA | Low | medium |
| [In-Memory Database with SQL Operations](../1p3a-qbanks/openai/raw/in-memory-database-sql.txt) | OpenAI | Low | medium |
| [Resumable Iterator with Multi-Dimensional Support](../1p3a-qbanks/openai/raw/resumable-iterator.txt) | OpenAI | Low | medium |
| [Multicore Overheat Prevention Controller](../1p3a-qbanks/optiver/raw/multicore-overheat-prevention-controller.txt) | Optiver | Low | hard |
| [Log Parser with Multi-Line Follow-up](../1p3a-qbanks/oracle/raw/log-parser-multiline.txt) | Oracle | Low | medium |
| [OA — In-Memory Banking System (4 Levels)](../1p3a-qbanks/ramp/raw/oa-banking-system.txt) | Ramp | Low | medium |
| [Moderator List Hierarchy](../1p3a-qbanks/reddit/raw/moderator-list-hierarchy.txt) | Reddit | Low | medium |
| [Friend / Money-Transfer Request Parsing (OOD)](../1p3a-qbanks/robinhood/raw/friend-money-transfer-ood.txt) | Robinhood | Low | medium |
| [Frontend Traffic Light (HTML / CSS / JS)](../1p3a-qbanks/salesforce/raw/frontend-traffic-light.txt) | Salesforce | Low | medium |
| [TypeScript Refactor OA (HackerRank — fullstack)](../1p3a-qbanks/salesforce/raw/typescript-refactor-oa.txt) | Salesforce | Low | medium |
| [SnowCal](../1p3a-qbanks/snowflake/raw/snowcal.txt) | Snowflake | Low | medium |
| [Rollback-Capable KV Store with Nested Transactions](../1p3a-qbanks/tesla/raw/rollback-kv-store-nested-transactions.txt) | Tesla | Low | medium |
| [In-Memory SQL-Like Database](../1p3a-qbanks/twosigma/raw/in-memory-sql-like-database.txt) | Two Sigma | Low | medium |
| [Phone Screen: Nested Add/Sub Expression Evaluator](../1p3a-qbanks/uber/raw/phone-screen-expression-calculator.txt) | Uber | Low | medium |
| [In-Memory Database — Levels 1–4 (TTL + Backup/Restore)](../1p3a-qbanks/xai/raw/in-memory-database-ttl-backup.txt) | xAI | Low | medium |
| [In-Memory Object Manager OA](../1p3a-qbanks/airbnb/raw/oa-object-manager-prefix-ttl.txt) | Airbnb | Single report | medium |
| [In-Memory SQL Database](../1p3a-qbanks/airbnb/raw/in-memory-sql-database.txt) | Airbnb | Single report | hard |
| [Multi-Stream Round-Robin Iterator](../1p3a-qbanks/airbnb/raw/multi-stream-iterator.txt) | Airbnb | Single report | medium |
| [Nested-List Iterator](../1p3a-qbanks/airbnb/raw/nested-list-iterator-with-remove.txt) | Airbnb | Single report | medium |
| [Parcel Tracking System](../1p3a-qbanks/airbnb/raw/oa-parcel-tracking-system.txt) | Airbnb | Single report | hard |
| [Recipe Management System OA](../1p3a-qbanks/airbnb/raw/oa-recipe-management-system.txt) | Airbnb | Single report | hard |
| [Enemy Factory with Shared Instances](../1p3a-qbanks/akunacapital/raw/enemy-factory-shared-instances.txt) | Akuna Capital | Single report | medium |
| [Portfolio Rebalancer](../1p3a-qbanks/akunacapital/raw/portfolio-rebalancer.txt) | Akuna Capital | Single report | easy |
| [Employee Total-Cost Object Model](../1p3a-qbanks/amazon/raw/employee-total-cost-object-model.txt) | Amazon | Single report | medium |
| [Event Filter & Queue Routing Service](../1p3a-qbanks/amazon/raw/event-filter-routing-service.txt) | Amazon | Single report | medium |
| [Extensible Cinema Ticket Pricing](../1p3a-qbanks/amazon/raw/cinema-ticket-pricing-ood.txt) | Amazon | Single report | medium |
| [Line Intersection Points](../1p3a-qbanks/amazon/raw/line-intersection-points.txt) | Amazon | Single report | medium |
| [Package Tracking & Status Aggregator Engine](../1p3a-qbanks/amazon/raw/package-tracking-status-aggregator.txt) | Amazon | Single report | medium |
| [Recursive Schema Validation](../1p3a-qbanks/amazon/raw/schema-validation-recursive.txt) | Amazon | Single report | hard |
| [Timestamped Key-Value Store](../1p3a-qbanks/amazon/raw/timestamped-key-value-store.txt) | Amazon | Single report | – |
| [Airplane Seat Reservation API](../1p3a-qbanks/apple/raw/airplane-seat-reservation-api.txt) | Apple | Single report | medium |
| [Deck Shuffle and Draw OOD](../1p3a-qbanks/apple/raw/deck-shuffle-and-draw-ood.txt) | Apple | Single report | medium |
| [Insert Delete GetRandom O(1)](../1p3a-qbanks/apple/raw/insert-delete-getrandom-o-1.txt) | Apple | Single report | medium |
| [Design Underground System](../1p3a-qbanks/bloomberg/raw/design-underground-system.txt) | Bloomberg | Single report | medium |
| [Simplified Grep](../1p3a-qbanks/bloomberg/raw/simplified-grep.txt) | Bloomberg | Single report | medium |
| [Time Based Key-Value Store (LeetCode 981)](../1p3a-qbanks/bytedance/raw/time-based-key-value-store.txt) | ByteDance / TikTok | Single report | – |
| [Deduplicating File System](../1p3a-qbanks/citadel/raw/deduplicating-file-system.txt) | Citadel / Citadel Securities | Single report | – |
| [Multi-Exchange BBO / NBBO Class](../1p3a-qbanks/citadel/raw/multi-exchange-nbbo-class.txt) | Citadel / Citadel Securities | Single report | medium |
| [Frontend Tech Execution — Reusable Dropdown Component](../1p3a-qbanks/coinbase/raw/fe-dropdown-tech-execution.txt) | Coinbase | Single report | hard |
| [Recipe Management System OA](../1p3a-qbanks/coinbase/raw/recipe-management-oa.txt) | Coinbase | Single report | medium |
| [Worker Salary OA](../1p3a-qbanks/coinbase/raw/worker-salary-oa.txt) | Coinbase | Single report | medium |
| [Code Craft: Cart with Promotions (OOD)](../1p3a-qbanks/doordash/raw/code-craft-cart-promotions-ood.txt) | DoorDash | Single report | medium |
| [Code Craft: Validate Cart](../1p3a-qbanks/doordash/raw/code-craft-validate-cart.txt) | DoorDash | Single report | medium |
| [Babylon Tile-Stack Merge Game](../1p3a-qbanks/google/raw/babylon-stack-merge-game.txt) | Google | Single report | hard |
| [Next-Word Predictor](../1p3a-qbanks/google/raw/next-word-frequency-predictor.txt) | Google | Single report | – |
| [Restaurant Waitlist: join / delete / find_first_match](../1p3a-qbanks/google/raw/waitlist-data-structure-with-table-size-match.txt) | Google | Single report | medium |
| [OA: Banking Account System](../1p3a-qbanks/instacart/raw/oa-banking-account-system.txt) | Instacart | Single report | hard |
| [Transactions Status Report SQL](../1p3a-qbanks/intuit/raw/transactions-status-report-sql.txt) | Intuit | Single report | medium |
| [C++ Virtual / Destructor / Pointer Review](../1p3a-qbanks/jpmorgan/raw/cpp-virtual-destructor-pointer-review.txt) | JPMorgan Chase | Single report | medium |
| [Sparse Vector / Matrix Product](../1p3a-qbanks/linkedin/raw/coding-sparse-vector-matrix-product.txt) | LinkedIn | Single report | hard |
| [Frontend Incremental Clicker with Configurable Buttons](../1p3a-qbanks/lyft/raw/frontend-incremental-clicker-table.txt) | Lyft | Single report | medium |
| [Valid Number](../1p3a-qbanks/meta/raw/lc-valid-number.txt) | Meta | Single report | medium |
| [KV Store with Snapshot Versions](../1p3a-qbanks/microsoft/raw/kv-store-with-snapshot.txt) | Microsoft | Single report | hard |
| [Parse Email Addresses from a Log String](../1p3a-qbanks/microsoft/raw/parse-emails-state-machine.txt) | Microsoft | Single report | hard |
| [Music Playlist History](../1p3a-qbanks/netflix/raw/music-playlist-history.txt) | Netflix | Single report | medium |
| [String to Integer (atoi)](../1p3a-qbanks/netflix/raw/string-to-integer-atoi.txt) | Netflix | Single report | medium |
| [CD Rental System (Java DSA / LLD)](../1p3a-qbanks/okta/raw/cd-rental-system.txt) | Okta | Single report | medium |
| [Memory Allocator (Allocate / Free)](../1p3a-qbanks/okta/raw/memory-allocator.txt) | Okta | Single report | medium |
| [NASDAQ Market Data Price-Level Book](../1p3a-qbanks/optiver/raw/nasdaq-market-data-price-level-book.txt) | Optiver | Single report | medium |
| [PowerBank Rack & Cell Management](../1p3a-qbanks/optiver/raw/powerbank-rack-cell-management.txt) | Optiver | Single report | hard |
| [Apply List of Operations (Command Pattern)](../1p3a-qbanks/oracle/raw/apply-operations-command-pattern.txt) | Oracle | Single report | medium |
| [OOD — Body-Temperature Measurement Classes](../1p3a-qbanks/oracle/raw/ood-patient-temperature-classes.txt) | Oracle | Single report | medium |
| [OOD — File Management System](../1p3a-qbanks/oracle/raw/ood-file-management-system.txt) | Oracle | Single report | easy |
| [Simplified Redis-Like KV / List Store](../1p3a-qbanks/oracle/raw/simplified-redis-kv-store.txt) | Oracle | Single report | medium |
| [Token Expiration Service](../1p3a-qbanks/oracle/raw/token-expiration-service.txt) | Oracle | Single report | – |
| [Newline-Split Stream Reader](../1p3a-qbanks/pinterest/raw/newline-stream-line-reader.txt) | Pinterest | Single report | medium |
| [Sparse Matrix Class (Store / Print / Add / Multiply)](../1p3a-qbanks/pinterest/raw/sparse-matrix-class.txt) | Pinterest | Single report | medium |
| [OA — Cloud File-Storage System (4 Levels)](../1p3a-qbanks/ramp/raw/oa-cloud-storage-system.txt) | Ramp | Single report | medium |
| [OA — Digital Recipe Manager (4 Levels)](../1p3a-qbanks/ramp/raw/oa-recipe-manager.txt) | Ramp | Single report | medium |
| [OA — Task Management System (4 Levels)](../1p3a-qbanks/ramp/raw/oa-task-management-system.txt) | Ramp | Single report | medium |
| [Pseudo-Memcached Protocol Server](../1p3a-qbanks/reddit/raw/pseudo-memcached-protocol.txt) | Reddit | Single report | medium |
| [In-Memory Key-Value Store with Transactions](../1p3a-qbanks/rippling/raw/in-memory-kv-store-transactions.txt) | Rippling | Single report | medium |
| [Flatten Nested Array](../1p3a-qbanks/roblox/raw/flatten-nested-array.txt) | Roblox | Single report | medium |
| [Optimal Account Balancing (LeetCode 465)](../1p3a-qbanks/salesforce/raw/optimal-account-balancing.txt) | Salesforce | Single report | hard |
| [Text Editor (AI-Assisted Pair Programming)](../1p3a-qbanks/shopify/raw/text-editor-output-formats.txt) | Shopify | Single report | – |
| [Custom List, Iterator & Map Function](../1p3a-qbanks/snapchat/raw/custom-list-iterator-and-map-function.txt) | Snapchat | Single report | medium |
| [Document Store with Predicate Query](../1p3a-qbanks/snowflake/raw/doc-predicate-store.txt) | Snowflake | Single report | hard |
| [Filter System with Dynamic Blacklist](../1p3a-qbanks/snowflake/raw/filter-system-dynamic-blacklist.txt) | Snowflake | Single report | medium |
| [Highway Sensor Journey Count](../1p3a-qbanks/sofi/raw/highway-sensor-journey-count.txt) | SoFi | Single report | medium |
| [Kanban Board Task Actions](../1p3a-qbanks/sofi/raw/kanban-board-task-actions.txt) | SoFi | Single report | medium |
| [Feature Flag SDK](../1p3a-qbanks/stripe/raw/feature-flag-sdk.txt) | Stripe | Single report | medium |
| [Compare Two Structs / Classes for Equality](../1p3a-qbanks/uber/raw/compare-two-structs-or-classes.txt) | Uber | Single report | medium |
| [OOD: Random Bingo Card Generator](../1p3a-qbanks/uber/raw/bingo-card-generator.txt) | Uber | Single report | medium |
| [Versioned Social Network with Snapshots](../1p3a-qbanks/uber/raw/social-network-snapshot.txt) | Uber | Single report | medium |
| [Live Refactor: Function → Production Service](../1p3a-qbanks/walmartlabs/raw/function-to-service-refactor.txt) | Walmart Labs | Single report | – |
| [Battleship Board (React, Frontend Onsite)](../1p3a-qbanks/waymo/raw/battleship-react-frontend.txt) | Waymo | Single report | medium |
| [Merge Object Histories Across Two Tracking Systems](../1p3a-qbanks/waymo/raw/cross-system-object-tracker-history.txt) | Waymo | Single report | medium |
| [Sparse Matrix Left- and Right-Multiply Vector](../1p3a-qbanks/waymo/raw/sparse-matrix-vector-multiply.txt) | Waymo | Single report | medium |
| [Checkers Game — Backend System Design](../1p3a-qbanks/xai/raw/checkers-backend-design.txt) | xAI | Single report | medium |
| [Durable KV Cache](../1p3a-qbanks/xai/raw/durable-kv-cache.txt) | xAI | Single report | medium |
| [Resumable Iterator with Save / Restore State](../1p3a-qbanks/xai/raw/resumable-iterator-design.txt) | xAI | Single report | medium |
| [Transactional Key-Value Store](../1p3a-qbanks/xai/raw/transactional-key-value-store.txt) | xAI | Single report | medium |

</details>

<details><summary><b>window-pointers-prefix</b> (118)</summary>

| Question | Company | Freq | Level |
|---|---|---|---|
| [Coding Q3 — Stack Trace Reconstruction](../1p3a-qbanks/anthropic/raw/coding-q3-stack-trace.txt) | Anthropic | High | medium |
| [KV Store With Rolling QPS](../1p3a-qbanks/databricks/raw/kv-store-rolling-qps.txt) | Databricks | High | medium |
| [Rate Limiter (Sliding Window) with Per-Entity Follow-up](../1p3a-qbanks/roblox/raw/rate-limiter-sliding-window.txt) | Roblox | High | medium |
| [Array Challenge: Left-Comparison Running Counter](../1p3a-qbanks/akunacapital/raw/left-comparison-running-counter.txt) | Akuna Capital | Medium | medium |
| [Drone Delivery on a Hub Ring — Minimum Travel](../1p3a-qbanks/amazon/raw/drone-hub-ring-min-travel.txt) | Amazon | Medium | medium |
| [Max Money from K Consecutive Bags](../1p3a-qbanks/amazon/raw/max-money-k-consecutive-bags.txt) | Amazon | Medium | medium |
| [Stream Moving Average with Recent-Value Weighting](../1p3a-qbanks/atlassian/raw/stream-moving-average-weighted-recent-values.txt) | Atlassian | Medium | medium |
| [Valid Palindrome II](../1p3a-qbanks/meta/raw/valid-palindrome-ii.txt) | Meta | Medium | easy |
| [Duplicate Detection I / II / III](../1p3a-qbanks/netflix/raw/duplicate-detection-three-part-series.txt) | Netflix | Medium | medium |
| [Chat Message Merge with Windowed API](../1p3a-qbanks/reddit/raw/chat-message-merge.txt) | Reddit | Medium | medium |
| [Phone Screen: Longest Subarray with Bounded Diff](../1p3a-qbanks/uber/raw/phone-screen-longest-stable-subarray.txt) | Uber | Medium | medium |
| [Segregate Binary String (Move Ones to End)](../1p3a-qbanks/akunacapital/raw/segregate-binary-string-move-ones.txt) | Akuna Capital | Low | medium |
| [Minimum Redistribution Cost on a Ring](../1p3a-qbanks/amazon/raw/min-redistribution-cost-ring.txt) | Amazon | Low | medium |
| [CodeSignal SWE OA (Spring 2026 Bank)](../1p3a-qbanks/bytedance/raw/codesignal-swe-oa-spring-2026.txt) | ByteDance / TikTok | Low | hard |
| [Longest Substring with At Most K Distinct Characters](../1p3a-qbanks/bytedance/raw/longest-substring-k-distinct.txt) | ByteDance / TikTok | Low | easy |
| [Sawtooth (Alternating Parity) Subarray Count](../1p3a-qbanks/capitalone/raw/sawtooth-subarray-count.txt) | Capital One | Low | medium |
| [Anagrammed indexOf](../1p3a-qbanks/databricks/raw/anagrammed-indexof.txt) | Databricks | Low | medium |
| [DE / AE Screen: SQL + Python (Sliding Window + Joins)](../1p3a-qbanks/doordash/raw/de-ae-sql-python-screen.txt) | DoorDash | Low | medium |
| [String Compression (Run-Length)](../1p3a-qbanks/goldmansachs/raw/string-compression.txt) | Goldman Sachs | Low | medium |
| [Trapping Rain Water](../1p3a-qbanks/goldmansachs/raw/trapping-rain-water.txt) | Goldman Sachs | Low | hard |
| [Piano Hand Movement Grouping](../1p3a-qbanks/google/raw/piano-hand-movement-grouping.txt) | Google | Low | medium |
| [Sliding-Window Rate Limiter / Abuse IP Detection](../1p3a-qbanks/ibm/raw/sliding-window-rate-limiter-and-abuse-ips.txt) | IBM | Low | medium |
| [Minimum Window Substring](../1p3a-qbanks/lyft/raw/minimum-window-substring.txt) | Lyft | Low | hard |
| [Subarray Sum Equals K](../1p3a-qbanks/meta/raw/subarray-sum-equals-k.txt) | Meta | Low | medium |
| [Rate Limiter (Open-Ended Coding)](../1p3a-qbanks/reddit/raw/rate-limiter-open-ended.txt) | Reddit | Low | medium |
| [Sliding Window: Target Containment and Most-Repeated Window](../1p3a-qbanks/roblox/raw/sliding-window-target-and-most-repeat.txt) | Roblox | Low | medium |
| [Rate Limiter — Return Dropped Times](../1p3a-qbanks/snowflake/raw/rate-limiter-dropped-requests.txt) | Snowflake | Low | medium |
| [OA: Maximum Items by Budget with Prefix Sums](../1p3a-qbanks/uber/raw/oa-prefix-sum-budget-purchase.txt) | Uber | Low | easy |
| [OA: Shortest Subarray with At Least K Distinct](../1p3a-qbanks/uber/raw/oa-shortest-subarray-k-distinct.txt) | Uber | Low | medium |
| [Phone Screen: Squares of a Sorted Array](../1p3a-qbanks/uber/raw/phone-screen-squares-of-sorted.txt) | Uber | Low | easy |
| [Moving Average Signal System](../1p3a-qbanks/akunacapital/raw/moving-average-signal-system.txt) | Akuna Capital | Single report | medium |
| [Sliding-Window Order Message Rate Limiter](../1p3a-qbanks/akunacapital/raw/sliding-window-order-message-rate-limiter.txt) | Akuna Capital | Single report | medium |
| [Circular Array — Multi-Query Distance Accumulation](../1p3a-qbanks/amazon/raw/circular-array-prefix-sum-multi-query.txt) | Amazon | Single report | easy |
| [Find Anagrams in a Character Stream](../1p3a-qbanks/amazon/raw/anagrams-in-string-stream.txt) | Amazon | Single report | medium |
| [Find Conflicting Events](../1p3a-qbanks/amazon/raw/find-conflicting-events.txt) | Amazon | Single report | medium |
| [Max-Sum Window over a Compressed Array](../1p3a-qbanks/amazon/raw/max-sum-window-run-length-segments.txt) | Amazon | Single report | hard |
| [Merge Two Sorted Arrays In-Place](../1p3a-qbanks/amazon/raw/merge-sorted-arrays-in-place.txt) | Amazon | Single report | easy |
| [Smallest Time-Diff Pair Per User (Orders × Ads)](../1p3a-qbanks/amazon/raw/smallest-time-diff-orders-ads.txt) | Amazon | Single report | medium |
| [Container With Most Water](../1p3a-qbanks/apple/raw/container-with-most-water.txt) | Apple | Single report | medium |
| [Design Hit Counter](../1p3a-qbanks/apple/raw/design-hit-counter.txt) | Apple | Single report | medium |
| [Longest Mountain in Array](../1p3a-qbanks/apple/raw/longest-mountain-in-array.txt) | Apple | Single report | medium |
| [Longest Substring Without Repeating Characters](../1p3a-qbanks/apple/raw/longest-substring-without-repeating-characters.txt) | Apple | Single report | medium |
| [One Edit Distance](../1p3a-qbanks/apple/raw/one-edit-distance.txt) | Apple | Single report | medium |
| [Product of Array Except Self](../1p3a-qbanks/apple/raw/product-of-array-except-self.txt) | Apple | Single report | medium |
| [Subarray Sum Equals K](../1p3a-qbanks/apple/raw/subarray-sum-equals-k.txt) | Apple | Single report | medium |
| [Trapping Rain Water](../1p3a-qbanks/apple/raw/trapping-rain-water.txt) | Apple | Single report | hard |
| [Duplicate Records Within a Time Window](../1p3a-qbanks/bloomberg/raw/duplicate-records-time-window.txt) | Bloomberg | Single report | medium |
| [Friends Of Appropriate Ages](../1p3a-qbanks/bloomberg/raw/friends-of-appropriate-ages.txt) | Bloomberg | Single report | medium |
| [Contiguous Subarray with Largest Min + Max](../1p3a-qbanks/bytedance/raw/largest-min-max-subarray.txt) | ByteDance / TikTok | Single report | medium |
| [Longest Repeating Character Replacement](../1p3a-qbanks/bytedance/raw/longest-repeating-character-replacement.txt) | ByteDance / TikTok | Single report | medium |
| [Longest Substring Without Repeating Characters](../1p3a-qbanks/bytedance/raw/longest-substring-without-repeating-characters.txt) | ByteDance / TikTok | Single report | medium |
| [Minimum Window Substring (LeetCode 76)](../1p3a-qbanks/bytedance/raw/minimum-window-substring.txt) | ByteDance / TikTok | Single report | hard |
| [Reverse Words in a String (Preserve Spaces, In-Place)](../1p3a-qbanks/bytedance/raw/reverse-words-in-place.txt) | ByteDance / TikTok | Single report | medium |
| [Sliding Window Maximum](../1p3a-qbanks/bytedance/raw/sliding-window-maximum.txt) | ByteDance / TikTok | Single report | hard |
| [Subarray Sum Equals K (Count)](../1p3a-qbanks/bytedance/raw/subarray-sum-equals-k.txt) | ByteDance / TikTok | Single report | medium |
| [Cumulative Views Reach Target Day](../1p3a-qbanks/capitalone/raw/cumulative-views-target-day.txt) | Capital One | Single report | easy |
| [Alternating String Merge](../1p3a-qbanks/cisco/raw/alternating-string-merge.txt) | Cisco | Single report | easy |
| [Longest Substring Without Repeating Characters (LC 3)](../1p3a-qbanks/cisco/raw/longest-unique-substring.txt) | Cisco | Single report | medium |
| [Minimum Swaps for Even / Odd Partition](../1p3a-qbanks/cisco/raw/minimum-swaps-even-odd-partition.txt) | Cisco | Single report | easy |
| [Application-to-Exchange Routing OOD with Sliding-Window Alerts](../1p3a-qbanks/citadel/raw/application-exchange-routing-alerting.txt) | Citadel / Citadel Securities | Single report | medium |
| [Real-Time Trading Event Feed Dashboard (React)](../1p3a-qbanks/citadel/raw/react-trading-event-feed-dashboard.txt) | Citadel / Citadel Securities | Single report | medium |
| [Code Craft: Chef Skill → Dish Profit Assignment](../1p3a-qbanks/doordash/raw/code-craft-chef-dish-profit-assignment.txt) | DoorDash | Single report | medium |
| [Compliance Alert / Trailing-Average Sliding Window](../1p3a-qbanks/goldmansachs/raw/compliance-alert-sliding-window.txt) | Goldman Sachs | Single report | easy |
| [Longest Subarray With Sum ≤ K](../1p3a-qbanks/goldmansachs/raw/longest-subarray-sum-at-most-k.txt) | Goldman Sachs | Single report | easy |
| [Longest Substring of All Same Letter](../1p3a-qbanks/goldmansachs/raw/longest-same-letter-substring.txt) | Goldman Sachs | Single report | easy |
| [Transaction Authorizer / Fraud Checks](../1p3a-qbanks/goldmansachs/raw/transaction-authorizer-fraud-checks.txt) | Goldman Sachs | Single report | medium |
| [Transaction Segments / Increasing Subarrays of Length K](../1p3a-qbanks/goldmansachs/raw/transaction-segments-increasing-subarrays.txt) | Goldman Sachs | Single report | easy |
| [Necklace Cut into Two Halves with Equal D/R Counts](../1p3a-qbanks/google/raw/necklace-cut-equal-counts.txt) | Google | Single report | hard |
| [Run-Length Vector Storage + Dot Product](../1p3a-qbanks/google/raw/compressed-vector-dot-product.txt) | Google | Single report | medium |
| [Streaming Points: Emit K Within Distance](../1p3a-qbanks/google/raw/streaming-points-k-within-distance.txt) | Google | Single report | medium |
| [Sum of Good Subarrays (Adjacent Diff ±1)](../1p3a-qbanks/google/raw/good-subarrays-diff-one.txt) | Google | Single report | medium |
| [Temperature Monitoring Data Structure](../1p3a-qbanks/google/raw/temperature-monitor-moving-window.txt) | Google | Single report | – |
| [Two-Day Log Intersection under Memory Limit](../1p3a-qbanks/google/raw/two-logs-intersection-memory-limit.txt) | Google | Single report | medium |
| [Minimum Length Subarray with K Distinct Numbers](../1p3a-qbanks/ibm/raw/minimum-length-subarray-with-k-distinct.txt) | IBM | Single report | medium |
| [Circular Active Computers Window](../1p3a-qbanks/jpmorgan/raw/circular-active-computers-window.txt) | JPMorgan Chase | Single report | easy |
| [Delete-One-Character Positions](../1p3a-qbanks/jpmorgan/raw/delete-one-character-positions.txt) | JPMorgan Chase | Single report | easy |
| [Split Array: Left Sum Greater](../1p3a-qbanks/jpmorgan/raw/split-array-left-sum-greater.txt) | JPMorgan Chase | Single report | easy |
| [Zero Compaction Array Coding](../1p3a-qbanks/jpmorgan/raw/zero-compaction-array-coding.txt) | JPMorgan Chase | Single report | easy |
| [2-D Matrix Range Sum with Updates (OA SumTable)](../1p3a-qbanks/linkedin/raw/coding-prefix-sum-2d-matrix-oa.txt) | LinkedIn | Single report | medium |
| [Equal-Pairing Product Sum](../1p3a-qbanks/mathworks/raw/equal-pairing-product-sum.txt) | MathWorks | Single report | easy |
| [Login Arrays Segment Compression](../1p3a-qbanks/mathworks/raw/login-array-segment-compression.txt) | MathWorks | Single report | medium |
| [Continuous Subarray Sum](../1p3a-qbanks/meta/raw/continuous-subarray-sum.txt) | Meta | Single report | medium |
| [Minimum Window Substring](../1p3a-qbanks/meta/raw/minimum-window-substring.txt) | Meta | Single report | hard |
| [Sliding-Window Stream Session Bucketing](../1p3a-qbanks/microsoft/raw/sliding-window-stream-bucketing.txt) | Microsoft | Single report | medium |
| [Count Substrings Without Repeating Characters](../1p3a-qbanks/nvidia/raw/count-substrings-without-repeating-characters.txt) | NVIDIA | Single report | easy |
| [Error Rate Window Monitor](../1p3a-qbanks/netflix/raw/error-rate-window-monitor.txt) | Netflix | Single report | easy |
| [Anagram Word Replacement in Phrases](../1p3a-qbanks/oracle/raw/anagram-phrase-replacement.txt) | Oracle | Single report | medium |
| [Container With Most Water (LC 11)](../1p3a-qbanks/oracle/raw/container-with-most-water.txt) | Oracle | Single report | medium |
| [Sort Array by Parity — Minimum Swaps](../1p3a-qbanks/oracle/raw/sort-array-by-parity-min-swaps.txt) | Oracle | Single report | medium |
| [Put Boxes Into the Warehouse (LC 1564)](../1p3a-qbanks/pinterest/raw/put-boxes-into-warehouse.txt) | Pinterest | Single report | medium |
| [Subarray Score ≤ K (Sliding-Window)](../1p3a-qbanks/pinterest/raw/subarray-score-at-most-k.txt) | Pinterest | Single report | medium |
| [OA — Sliding-Window Rate Limiter](../1p3a-qbanks/ramp/raw/oa-rate-limiter.txt) | Ramp | Single report | medium |
| [Shortest Palindrome](../1p3a-qbanks/reddit/raw/shortest-palindrome.txt) | Reddit | Single report | hard |
| [Maximize Distance to Closest Person (LeetCode 849)](../1p3a-qbanks/roblox/raw/maximize-distance-to-closest-person.txt) | Roblox | Single report | easy |
| [Minimum Height Difference Between Distant Peaks](../1p3a-qbanks/roblox/raw/minimum-height-difference-between-distant-peaks.txt) | Roblox | Single report | medium |
| [Maximum Requests in a Time Window (OA)](../1p3a-qbanks/salesforce/raw/maximum-requests-in-time-window.txt) | Salesforce | Single report | easy |
| [String Compression (HackerRank OA, LeetCode 443)](../1p3a-qbanks/salesforce/raw/string-compression-oa.txt) | Salesforce | Single report | medium |
| [Stream Window Max Unique Users](../1p3a-qbanks/snapchat/raw/stream-window-max-unique-users.txt) | Snapchat | Single report | medium |
| [Document Target Coverage and Minimum Window](../1p3a-qbanks/snowflake/raw/document-target-coverage-and-minimum-window.txt) | Snowflake | Single report | medium |
| [Find All Anagrams in a String](../1p3a-qbanks/snowflake/raw/find-all-anagrams-in-a-string.txt) | Snowflake | Single report | medium |
| [Recipe as Contiguous Ingredient Subsequence](../1p3a-qbanks/snowflake/raw/recipe-ingredient-subsequence.txt) | Snowflake | Single report | medium |
| [RecordCollection Sort Colors](../1p3a-qbanks/snowflake/raw/recordcollection-sort-colors.txt) | Snowflake | Single report | medium |
| [Reverse Alphanumeric Segments](../1p3a-qbanks/snowflake/raw/reverse-alphanumeric-segments.txt) | Snowflake | Single report | easy |
| [Kth Unique Maximum from Two Sorted Arrays](../1p3a-qbanks/sofi/raw/kth-unique-maximum-from-two-sorted-arrays.txt) | SoFi | Single report | medium |
| [Longest Substring Without Repeating Characters](../1p3a-qbanks/pinduoduo/raw/longest-substring-one-allowed-repeat.txt) | Temu / Pinduoduo | Single report | medium |
| [Merge Step Functions of (timestamp, value) Pairs](../1p3a-qbanks/uber/raw/merge-step-functions.txt) | Uber | Single report | medium |
| [OA: Round-Trip Mission Schedule](../1p3a-qbanks/uber/raw/oa-codesignal-multi-mission-routing.txt) | Uber | Single report | easy |
| [Shortest Word Distance](../1p3a-qbanks/uber/raw/shortest-word-distance.txt) | Uber | Single report | easy |
| [Sliding Window Maximum](../1p3a-qbanks/uber/raw/sliding-window-maximum.txt) | Uber | Single report | hard |
| [Zero Array Transformation](../1p3a-qbanks/uber/raw/zero-array-transformation.txt) | Uber | Single report | medium |
| [Insert Spaces Around Palindrome Layers](../1p3a-qbanks/walmartlabs/raw/shrink-palindrome-spaces.txt) | Walmart Labs | Single report | easy |
| [Minimum Talent-Complete Team from Every Start](../1p3a-qbanks/walmartlabs/raw/minimum-talent-complete-team-from-each-start.txt) | Walmart Labs | Single report | medium |
| [Permutation Maximizing Σ B[i] where B[i] > A[i]](../1p3a-qbanks/walmartlabs/raw/permutation-max-greater-sum.txt) | Walmart Labs | Single report | medium |
| [Contains Duplicate III (Bucket Sort)](../1p3a-qbanks/waymo/raw/bucket-sort-contains-duplicate-iii.txt) | Waymo | Single report | medium |
| [Hashmap + Prefix Sum Subarray Round](../1p3a-qbanks/waymo/raw/hash-prefix-sum-coding.txt) | Waymo | Single report | easy |
| [LED Digit Display Invariant Under 180° Rotation](../1p3a-qbanks/waymo/raw/strobogrammatic-led-display.txt) | Waymo | Single report | easy |
| [Sort a Quadratic-Transformed Sorted Array](../1p3a-qbanks/waymo/raw/sort-transformed-quadratic-array.txt) | Waymo | Single report | medium |
| [K-th Element on a Streaming Time Window](../1p3a-qbanks/xai/raw/kth-element-streaming-window.txt) | xAI | Single report | medium |

</details>

<details><summary><b>parsing-simulation</b> (110)</summary>

| Question | Company | Freq | Level |
|---|---|---|---|
| [AI Code Craft Challenge: Delayed-Delivery Workflow Engine](../1p3a-qbanks/doordash/raw/ai-code-craft-workflow-engine.txt) | DoorDash | High | medium |
| [Proportional Allocation Strategy Backtest](../1p3a-qbanks/optiver/raw/proportional-allocation-backtest.txt) | Optiver | High | medium |
| [Bikemap Integration](../1p3a-qbanks/stripe/raw/bikemap-integration.txt) | Stripe | High | medium |
| [Invoice / Payment Reconciliation](../1p3a-qbanks/stripe/raw/invoice-payment-reconciliation.txt) | Stripe | High | medium |
| [Request Routing System (REGISTER / DISTANCE / ROUTE)](../1p3a-qbanks/stripe/raw/request-routing-haversine-oa.txt) | Stripe | High | medium |
| [Implement `uniq` — Unique Lines in a File](../1p3a-qbanks/vanta/raw/unique-lines-command.txt) | Vanta | High | medium |
| [Card Game Simulation](../1p3a-qbanks/affirm/raw/card-game-simulation.txt) | Affirm | Medium | medium |
| [Text Justification — Print as Table](../1p3a-qbanks/airbnb/raw/text-justification-table.txt) | Airbnb | Medium | medium |
| [URL Query-String Parser](../1p3a-qbanks/airbnb/raw/url-query-string-parser.txt) | Airbnb | Medium | easy |
| [Inventory Allocation by Bid Priority](../1p3a-qbanks/amazon/raw/inventory-allocation-bid-priority.txt) | Amazon | Medium | medium |
| [Pair Concatenation to Target](../1p3a-qbanks/capitalone/raw/pair-concatenation-to-target.txt) | Capital One | Medium | easy |
| [Flappy Bird (AI-Assisted Strategy OA)](../1p3a-qbanks/coinbase/raw/flappy-bird-ai-oa.txt) | Coinbase | Medium | medium |
| [Sort Documents on a 2D Plane](../1p3a-qbanks/figma/raw/sort-documents-left-to-right.txt) | Figma | Medium | medium |
| [OA: Collect Coins on 1D Board](../1p3a-qbanks/google/raw/oa-collect-coins-1d-board.txt) | Google | Medium | medium |
| [BitFont / Bitmap Renderer](../1p3a-qbanks/stripe/raw/bitfont-renderer.txt) | Stripe | Medium | medium |
| [Phone Screen: Find Robot Position from Blocker Distances](../1p3a-qbanks/uber/raw/phone-screen-robot-position-by-blocker-distance.txt) | Uber | Medium | medium |
| [Promotion Offer Redemption Tracking](../1p3a-qbanks/affirm/raw/promotion-offer-redemption.txt) | Affirm | Low | medium |
| [User Trust Score from Transaction Logs](../1p3a-qbanks/affirm/raw/user-trust-score-from-logs.txt) | Affirm | Low | medium |
| [Refund Waterfall Across Payment Methods](../1p3a-qbanks/airbnb/raw/refund-waterfall-payment-methods.txt) | Airbnb | Low | medium |
| [Round Price to Nearest Valid Tick](../1p3a-qbanks/akunacapital/raw/round-price-to-nearest-tick.txt) | Akuna Capital | Low | medium |
| [Coding — Bootloader Instruction Simulator (Fix the Loop)](../1p3a-qbanks/anthropic/raw/coding-bootloader.txt) | Anthropic | Low | medium |
| [OA — DNS Resolver (Fellows CodeSignal, Round 1)](../1p3a-qbanks/anthropic/raw/oa-fellows-dns-resolver.txt) | Anthropic | Low | medium |
| [Dynamic Two-Array Pair Sum Queries](../1p3a-qbanks/capitalone/raw/dynamic-two-array-pair-sum-queries.txt) | Capital One | Low | medium |
| [Repeated Leading-Nonzero Subtraction](../1p3a-qbanks/capitalone/raw/repeated-subtraction-zeroing.txt) | Capital One | Low | medium |
| [Implement `tac` — Reverse-Order File Reader](../1p3a-qbanks/citadel/raw/reverse-file-tac-implementation.txt) | Citadel / Citadel Securities | Low | medium |
| [Parse Log File by Thread Id](../1p3a-qbanks/coinbase/raw/parse-log-by-thread-id.txt) | Coinbase | Low | easy |
| [Resize Stacked Rectangles — Distribute Height](../1p3a-qbanks/figma/raw/resize-rectangles-distribute-height.txt) | Figma | Low | medium |
| [Mahjong Winning Hand Detection (14 Tiles)](../1p3a-qbanks/google/raw/mahjong-winning-hand-check.txt) | Google | Low | medium |
| [Robot Status Message Deduplication Window](../1p3a-qbanks/google/raw/robot-status-message-deduplication.txt) | Google | Low | easy |
| [Array Plus One with Large-Input Follow-Up](../1p3a-qbanks/intuit/raw/array-plus-one-with-large-input-follow-up.txt) | Intuit | Low | easy |
| [Bash Largest File from Listing Output](../1p3a-qbanks/intuit/raw/bash-largest-file-from-ls-output.txt) | Intuit | Low | medium |
| [Grouped Binary Substrings](../1p3a-qbanks/jpmorgan/raw/grouped-binary-substrings.txt) | JPMorgan Chase | Low | easy |
| [Minimum Anagram Edits](../1p3a-qbanks/jpmorgan/raw/minimum-anagram-edits.txt) | JPMorgan Chase | Low | easy |
| [AI Coding — Compiler Optimization](../1p3a-qbanks/meta/raw/ai-coding-compiler-optimization.txt) | Meta | Low | hard |
| [Palindrome Number + Next-Greater Palindrome](../1p3a-qbanks/microsoft/raw/palindrome-number-and-next-palindrome.txt) | Microsoft | Low | medium |
| [String Encoding / Decoding Variants](../1p3a-qbanks/nvidia/raw/string-encoding-decoding.txt) | NVIDIA | Low | easy |
| [JSON Path Query / Inverted Index](../1p3a-qbanks/netflix/raw/json-path-query-and-inverted-index.txt) | Netflix | Low | medium |
| [Experiment Bucket Allocation / Rebalancing](../1p3a-qbanks/pinterest/raw/experiment-bucket-allocation-rebalancing.txt) | Pinterest | Low | medium |
| [Implement round() From String + Round-to-Precision](../1p3a-qbanks/pinterest/raw/round-and-precision-string.txt) | Pinterest | Low | medium |
| [Detect Recurring Transactions](../1p3a-qbanks/ramp/raw/recurring-transactions.txt) | Ramp | Low | medium |
| [Admin Action Log: Order & Permission](../1p3a-qbanks/reddit/raw/admin-action-log-ordering.txt) | Reddit | Low | medium |
| [Fractional Share Buy/Sell Inventory](../1p3a-qbanks/robinhood/raw/fractional-share-inventory.txt) | Robinhood | Low | easy |
| [Word Frequency from a Large Paragraph](../1p3a-qbanks/robinhood/raw/word-frequency-large-paragraph.txt) | Robinhood | Low | medium |
| [Calculate Amount Paid in Taxes](../1p3a-qbanks/snowflake/raw/calculate-amount-paid-in-taxes.txt) | Snowflake | Low | easy |
| [Business Account Data Verification](../1p3a-qbanks/stripe/raw/kyc-business-verification.txt) | Stripe | Low | medium |
| [Chat Billing Calculation (OA)](../1p3a-qbanks/stripe/raw/chat-billing-oa.txt) | Stripe | Low | medium |
| [Playing Cards: Straight Flush / Missing Card](../1p3a-qbanks/pinduoduo/raw/playing-card-straight-flush.txt) | Temu / Pinduoduo | Low | medium |
| [Add Two Strings with a Single-Digit Adder](../1p3a-qbanks/twosigma/raw/add-two-strings-with-single-digit-adder.txt) | Two Sigma | Low | easy |
| [OA: Stairs vs Elevator Energy Trade-off](../1p3a-qbanks/uber/raw/oa-stairs-elevator-energy-tradeoff.txt) | Uber | Low | medium |
| [Phone Screen: Tennis Rounds — Print Round Winners](../1p3a-qbanks/uber/raw/phone-screen-tennis-rounds.txt) | Uber | Low | easy |
| [Process Raw CSV for Downstream Consumers](../1p3a-qbanks/waymo/raw/csv-parser-open-ended.txt) | Waymo | Low | medium |
| [Caesar Cipher with Case Preservation](../1p3a-qbanks/amazon/raw/caesar-cipher.txt) | Amazon | Single report | easy |
| [Lexicographically Minimal BoxIds via +1/9 Op](../1p3a-qbanks/amazon/raw/lex-min-box-ids-plus-one-mod-nine.txt) | Amazon | Single report | hard |
| [Message Latency from Send/Receive CSV Logs](../1p3a-qbanks/amazon/raw/message-latency-from-csv-logs.txt) | Amazon | Single report | easy |
| [Bag-of-Words Similarity Search from Scratch](../1p3a-qbanks/apple/raw/bag-of-words-similarity-search-from-scratch.txt) | Apple | Single report | medium |
| [Implement TF-IDF from Scratch](../1p3a-qbanks/apple/raw/implement-tf-idf-from-scratch.txt) | Apple | Single report | medium |
| [Substring Pattern Matches](../1p3a-qbanks/apple/raw/substring-pattern-matches.txt) | Apple | Single report | medium |
| [Robot Parts Assembly](../1p3a-qbanks/atlassian/raw/robot-parts-assembly.txt) | Atlassian | Single report | easy |
| [String to Integer (atoi)](../1p3a-qbanks/bloomberg/raw/string-to-integer-atoi.txt) | Bloomberg | Single report | medium |
| [Wordle Character Compare](../1p3a-qbanks/bloomberg/raw/wordle-character-compare.txt) | Bloomberg | Single report | medium |
| [AP Replacement Rounds](../1p3a-qbanks/capitalone/raw/ap-replacement-rounds.txt) | Capital One | Single report | medium |
| [Longest Same-Character Run](../1p3a-qbanks/capitalone/raw/longest-same-character-run.txt) | Capital One | Single report | easy |
| [Lunar Phase State Lookup](../1p3a-qbanks/capitalone/raw/lunar-phase-state.txt) | Capital One | Single report | easy |
| [Outside-In String Reordering](../1p3a-qbanks/capitalone/raw/outside-in-string-reordering.txt) | Capital One | Single report | easy |
| [Vowel-Wrapped Substring Middle Reverse](../1p3a-qbanks/capitalone/raw/vowel-wrap-reverse-middle.txt) | Capital One | Single report | easy |
| [W-D-L Outcome Reordering](../1p3a-qbanks/capitalone/raw/wdl-cyclic-reordering.txt) | Capital One | Single report | easy |
| [HFT Onsite Round 3 — Sort Complexity + Linux Fundamentals + tail(n) Design](../1p3a-qbanks/citadel/raw/citadel-hft-onsite-fundamentals.txt) | Citadel / Citadel Securities | Single report | hard |
| [Code Craft: Generate 5-Minute Day-Time Codes](../1p3a-qbanks/doordash/raw/code-craft-day-time-interval-codes.txt) | DoorDash | Single report | easy |
| [Data Engineer Screen — SQL Collaborator Model + IPv4 Validation](../1p3a-qbanks/figma/raw/data-engineer-sql-collaborator-ip-validation.txt) | Figma | Single report | medium |
| [Chairs / Restaurant Order Simulation](../1p3a-qbanks/goldmansachs/raw/chairs-restaurant-simulation.txt) | Goldman Sachs | Single report | medium |
| [Movement Command Controller](../1p3a-qbanks/goldmansachs/raw/movement-command-controller.txt) | Goldman Sachs | Single report | easy |
| [Plus-Multiply Even / Odd Parity](../1p3a-qbanks/goldmansachs/raw/plus-multiply-even-odd-parity.txt) | Goldman Sachs | Single report | medium |
| [Gate Entry / Exit Time Resolution with Tie-Break Rules](../1p3a-qbanks/google/raw/gate-entry-exit-scheduler.txt) | Google | Single report | medium |
| [Google Doc Line Wrap (Word Wrap with Whitespace and Newlines)](../1p3a-qbanks/google/raw/document-line-wrap.txt) | Google | Single report | medium |
| [OA: Email Checksum + Verification-String Lookup](../1p3a-qbanks/google/raw/oa-email-checksum-verification-string.txt) | Google | Single report | easy |
| [.NET OA: 3^x * 5^y Count and Decreasing Subarrays](../1p3a-qbanks/ibm/raw/dotnet-number-and-decreasing-subarray-oa.txt) | IBM | Single report | easy |
| [JSON Prediction Diff](../1p3a-qbanks/ibm/raw/json-prediction-diff.txt) | IBM | Single report | easy |
| [Minimum Insertions to Form Repeated abc Pattern](../1p3a-qbanks/ibm/raw/minimum-insertions-to-abc-pattern.txt) | IBM | Single report | easy |
| [Karat Coding: Max Adjacent Stock Price Change](../1p3a-qbanks/instacart/raw/karat-stock-price-change.txt) | Instacart | Single report | easy |
| [Onsite Coding: Product Pricing, Promotions & Shelf Ordering](../1p3a-qbanks/instacart/raw/onsite-product-pricing-promotions-shelf-ordering.txt) | Instacart | Single report | medium |
| [Clean String: Remove Punctuation and Stop Words](../1p3a-qbanks/intuit/raw/clean-string-remove-punctuation-stop-words.txt) | Intuit | Single report | easy |
| [Alphanumeric Vowel / Consonant Count](../1p3a-qbanks/jpmorgan/raw/alphanumeric-vowel-consonant-count.txt) | JPMorgan Chase | Single report | easy |
| [Alternating Triplets](../1p3a-qbanks/jpmorgan/raw/alternating-triplets.txt) | JPMorgan Chase | Single report | easy |
| [Markdown Table of Contents](../1p3a-qbanks/jpmorgan/raw/markdown-table-of-contents.txt) | JPMorgan Chase | Single report | easy |
| [Ranked-Choice Voting (Instant Runoff)](../1p3a-qbanks/linkedin/raw/coding-ranked-choice-voting.txt) | LinkedIn | Single report | medium |
| [Multiply Strings (LC 43)](../1p3a-qbanks/microsoft/raw/multiply-strings.txt) | Microsoft | Single report | medium |
| [Sort 0..32000 with Bit-Vector Storage](../1p3a-qbanks/microsoft/raw/bit-vector-sort-0-to-32000.txt) | Microsoft | Single report | medium |
| [Human-Readable Duration Timer](../1p3a-qbanks/netflix/raw/human-readable-duration-timer.txt) | Netflix | Single report | easy |
| [User Engagement Pattern Classification](../1p3a-qbanks/netflix/raw/user-engagement-pattern-classification.txt) | Netflix | Single report | medium |
| [Validate IPv4 Addresses in an Array](../1p3a-qbanks/okta/raw/ipv4-validation.txt) | Okta | Single report | easy |
| [Run-Length String Compression](../1p3a-qbanks/oracle/raw/run-length-string-compression.txt) | Oracle | Single report | easy |
| [Stream Deduplication with Near-Duplicate Detection](../1p3a-qbanks/perplexity/raw/stream-deduplication-with-near-duplicate-detection.txt) | Perplexity | Single report | medium |
| [Convert snake_case names to lowerCamelCase](../1p3a-qbanks/ramp/raw/snake-case-to-camelcase.txt) | Ramp | Single report | easy |
| [Tic Tac Toe](../1p3a-qbanks/ramp/raw/tic-tac-toe.txt) | Ramp | Single report | easy |
| [Distinct Cyclic Number Pairs](../1p3a-qbanks/roblox/raw/distinct-cyclic-number-pairs.txt) | Roblox | Single report | medium |
| [Binary String Synchronous '01' → '10' Replacement](../1p3a-qbanks/salesforce/raw/binary-string-01-10-swap.txt) | Salesforce | Single report | medium |
| [Delete One Character Type, Minimize Unique-Character Segments](../1p3a-qbanks/salesforce/raw/delete-character-minimize-unique-segments.txt) | Salesforce | Single report | medium |
| [Cows and Bulls (Guess the Number)](../1p3a-qbanks/shopify/raw/cows-and-bulls.txt) | Shopify | Single report | easy |
| [Grep With Context Lines](../1p3a-qbanks/snowflake/raw/grep-with-context-lines.txt) | Snowflake | Single report | medium |
| [Happy Number](../1p3a-qbanks/snowflake/raw/happy-number.txt) | Snowflake | Single report | easy |
| [Valid Tic-Tac-Toe State (Extended)](../1p3a-qbanks/snowflake/raw/tic-tac-toe-valid-state-extended.txt) | Snowflake | Single report | medium |
| [Karat Text Justification](../1p3a-qbanks/sofi/raw/karat-text-justification.txt) | SoFi | Single report | medium |
| [Rock-Paper-Scissors Lineup](../1p3a-qbanks/sofi/raw/rock-paper-scissors-lineup.txt) | SoFi | Single report | medium |
| [Account Balance Manager](../1p3a-qbanks/stripe/raw/account-balance-manager.txt) | Stripe | Single report | easy |
| [Financial Account Ledger (OA)](../1p3a-qbanks/stripe/raw/financial-account-ledger-oa.txt) | Stripe | Single report | – |
| [Http Request Language Preference](../1p3a-qbanks/stripe/raw/http-language-preference.txt) | Stripe | Single report | medium |
| [Matching Contacts](../1p3a-qbanks/stripe/raw/matching-contacts.txt) | Stripe | Single report | medium |
| [Payment Reconciliation](../1p3a-qbanks/stripe/raw/payment-reconciliation-integration.txt) | Stripe | Single report | medium |
| [Course Overlap by Student ID](../1p3a-qbanks/walmartlabs/raw/course-overlap.txt) | Walmart Labs | Single report | medium |
| [Longest Even-Length Word](../1p3a-qbanks/walmartlabs/raw/longest-even-length-word.txt) | Walmart Labs | Single report | easy |

</details>

<details><summary><b>ml-coding</b> (94)</summary>

| Question | Company | Freq | Level |
|---|---|---|---|
| [MLE Oral 八股: Dropout, Overfitting, Normalization, Loss Functions](../1p3a-qbanks/bytedance/raw/ml-knowledge-oral-cluster.txt) | ByteDance / TikTok | High | medium |
| [ML Fundamentals Deep Dive (AI/ML & MLE Roles)](../1p3a-qbanks/google/raw/ml-fundamentals-deep-dive.txt) | Google | High | – |
| [Autograd / Manual Backprop + Hillis-Steele Scan](../1p3a-qbanks/openai/raw/autograd-hillis-steele-scan.txt) | OpenAI | High | hard |
| [Classifier with Noisy Annotators](../1p3a-qbanks/openai/raw/classifier-noisy-annotators.txt) | OpenAI | High | medium |
| [NumPy Puzzle: 1-NN Vectorization → Wx+b Network](../1p3a-qbanks/openai/raw/numpy-1nn-wx-b.txt) | OpenAI | High | medium |
| [Transformer Bug Hunt](../1p3a-qbanks/openai/raw/transformer-bug-hunt.txt) | OpenAI | High | medium |
| [ML Fundamentals Quick-Fire](../1p3a-qbanks/pinterest/raw/ml-fundamentals-quick-fire.txt) | Pinterest | High | medium |
| [Project / Technical Deep Dive](../1p3a-qbanks/shopify/raw/project-technical-deep-dive.txt) | Shopify | High | – |
| [QR OA — NYC Temperature Regression](../1p3a-qbanks/twosigma/raw/qr-oa-nyc-temperature-regression.txt) | Two Sigma | High | medium |
| [ML Breadth Orals — Linear / Logistic / Random Forest / Optimizers](../1p3a-qbanks/amazon/raw/as-ml-breadth-orals.txt) | Amazon | Medium | medium |
| [Transformer / Attention Deep-Dive](../1p3a-qbanks/amazon/raw/transformer-attention-deep-dive.txt) | Amazon | Medium | medium |
| [ML Fundamentals & Model Debugging Drill](../1p3a-qbanks/apple/raw/ml-fundamentals-and-model-debugging.txt) | Apple | Medium | medium |
| [MLE CodeSignal OA (MCQ + NN + Classical-ML Coding)](../1p3a-qbanks/coinbase/raw/mle-codesignal-oa.txt) | Coinbase | Medium | medium |
| [MLE Onsite: ML Knowledge / Discussion Round](../1p3a-qbanks/doordash/raw/mle-ml-discussion-round.txt) | DoorDash | Medium | medium |
| [GPU and Inference Systems Fundamentals](../1p3a-qbanks/nvidia/raw/gpu-and-inference-systems-fundamentals.txt) | NVIDIA | Medium | medium |
| [Math Reasoning: Stopping Time / Las Vegas Algorithm](../1p3a-qbanks/openai/raw/math-reasoning-stopping-time.txt) | OpenAI | Medium | hard |
| [MLE Live Coding: EDA + Post-Category CTR](../1p3a-qbanks/reddit/raw/mle-eda-ctr-notebook.txt) | Reddit | Medium | medium |
| [MLE Project / Paper Deep Dive](../1p3a-qbanks/roblox/raw/mle-project-deep-dive.txt) | Roblox | Medium | medium |
| [Project Deep Dive and Paper Presentation](../1p3a-qbanks/scale.ai/raw/project-deep-dive-and-paper-presentation.txt) | Scale AI | Medium | medium |
| [ML Fundamentals, Transformer & Regularization](../1p3a-qbanks/snapchat/raw/ml-fundamentals-transformer-and-regularization.txt) | Snapchat | Medium | medium |
| [QR Data Analysis Prediction Case](../1p3a-qbanks/twosigma/raw/qr-data-analysis-prediction-case.txt) | Two Sigma | Medium | medium |
| [MLE Onsite: ML Coding from Scratch (Regression / Markov / Facility)](../1p3a-qbanks/uber/raw/mle-onsite-ml-coding-from-scratch.txt) | Uber | Medium | medium |
| [Hand-Write K-Means Clustering (NumPy)](../1p3a-qbanks/waymo/raw/ml-coding-handwrite-kmeans.txt) | Waymo | Medium | medium |
| [LoRA and PEFT Variants](../1p3a-qbanks/amazon/raw/lora-and-peft-variants.txt) | Amazon | Low | medium |
| [RLHF: PPO vs GRPO vs GSPO](../1p3a-qbanks/amazon/raw/rlhf-ppo-grpo-gspo.txt) | Amazon | Low | hard |
| [Science Application (Ambiguous Problem)](../1p3a-qbanks/amazon/raw/science-application-ambiguous-problem.txt) | Amazon | Low | – |
| [ML Programming Screen — QKV Attention & einsum](../1p3a-qbanks/anthropic/raw/ml-programming-screen.txt) | Anthropic | Low | hard |
| [ML Take-Home (4–5 hours)](../1p3a-qbanks/anthropic/raw/ml-take-home-research.txt) | Anthropic | Low | hard |
| [Performance Modeling — Matmul Arithmetic Intensity](../1p3a-qbanks/anthropic/raw/performance-engineer-modeling.txt) | Anthropic | Low | hard |
| [RL Fundamentals — GRPO Debug](../1p3a-qbanks/anthropic/raw/rl-fundamentals-grpo-debug.txt) | Anthropic | Low | hard |
| [Transformer Attention Mask and Heads Coding](../1p3a-qbanks/apple/raw/transformer-attention-mask-and-heads-coding.txt) | Apple | Low | medium |
| [Vectorized K-Means in NumPy or PyTorch](../1p3a-qbanks/apple/raw/vectorized-k-means-in-numpy-or-pytorch.txt) | Apple | Low | medium |
| [Hand-Code Self-Attention and Cross-Entropy](../1p3a-qbanks/bytedance/raw/self-attention-cross-entropy-pseudocode.txt) | ByteDance / TikTok | Low | hard |
| [MLE Deployment & Latency (Set B)](../1p3a-qbanks/capitalone/raw/mle-deployment-and-latency.txt) | Capital One | Low | – |
| [MLE ML Knowledge Orals (Set A)](../1p3a-qbanks/capitalone/raw/mle-ml-knowledge-orals.txt) | Capital One | Low | – |
| [Linear Regression Gradient Descent](../1p3a-qbanks/databricks/raw/linear-regression-gradient-descent.txt) | Databricks | Low | medium |
| [ML Coding — BERT Span / Keyphrase Extraction](../1p3a-qbanks/figma/raw/bert-keyphrase-span-extraction.txt) | Figma | Low | medium |
| [MLE Screen: Coding + ML Concepts](../1p3a-qbanks/instacart/raw/mle-coding-and-ml-concepts.txt) | Instacart | Low | medium |
| [Hand-Written K-Means](../1p3a-qbanks/microsoft/raw/handwritten-k-means.txt) | Microsoft | Low | medium |
| [Deep Learning Fundamentals: Optimization, Drift, Calibration](../1p3a-qbanks/nvidia/raw/deep-learning-fundamentals.txt) | NVIDIA | Low | – |
| [ML Coding: 2-D Convolution, Decaying Attention, Training Loop](../1p3a-qbanks/nvidia/raw/ml-coding-conv-attention-training-loop.txt) | NVIDIA | Low | medium |
| [ML Research Orals (Self-Attention / LoRA / Optimizers)](../1p3a-qbanks/netflix/raw/ml-research-orals.txt) | Netflix | Low | – |
| [Code Reading — 400-line PyTorch Refactor](../1p3a-qbanks/openai/raw/code-reading-pytorch-refactor.txt) | OpenAI | Low | – |
| [Sharded Matmul and Backprop Debugging](../1p3a-qbanks/openai/raw/sharded-matmul-backprop-debugging.txt) | OpenAI | Low | hard |
| [ML Intern OA (CodeSignal: MCQ + NN Forward + Coding + ML Implementations)](../1p3a-qbanks/pinterest/raw/mle-intern-codesignal-oa.txt) | Pinterest | Low | medium |
| [ML Fundamentals](../1p3a-qbanks/reddit/raw/ml-fundamentals-oral.txt) | Reddit | Low | – |
| [ML Modeling: Recommend Games to a Roblox User](../1p3a-qbanks/roblox/raw/ml-modeling-game-recommendation.txt) | Roblox | Low | hard |
| [LLM Post-Training Theory Conversation](../1p3a-qbanks/scale.ai/raw/llm-post-training-theory-conversation.txt) | Scale AI | Low | medium |
| [NumPy Top-p Sampling and Multi-Head Attention](../1p3a-qbanks/scale.ai/raw/numpy-top-p-sampling-and-multi-head-attention.txt) | Scale AI | Low | medium |
| [NumPy Conv2D Forward and Parameter Count](../1p3a-qbanks/tesla/raw/numpy-conv2d-forward.txt) | Tesla | Low | medium |
| [Bootstrap Mean & Confidence Interval](../1p3a-qbanks/amazon/raw/bootstrap-mean-confidence-interval.txt) | Amazon | Single report | medium |
| [Handwrite Sigmoid, Softmax, and Attention](../1p3a-qbanks/amazon/raw/handwrite-sigmoid-softmax-attention.txt) | Amazon | Single report | medium |
| [Handwritten InfoNCE in PyTorch](../1p3a-qbanks/amazon/raw/infonce-handwritten-pytorch.txt) | Amazon | Single report | medium |
| [NumPy Mean and Variance with MLE Follow-Up](../1p3a-qbanks/amazon/raw/numpy-mean-variance-mle.txt) | Amazon | Single report | – |
| [Paper Read Round (FAR / Premium Loops)](../1p3a-qbanks/amazon/raw/paper-read-round-far.txt) | Amazon | Single report | hard |
| [Implement an Image Filter](../1p3a-qbanks/apple/raw/implement-an-image-filter.txt) | Apple | Single report | medium |
| [Online Token Processing, Embeddings, and Classification](../1p3a-qbanks/apple/raw/online-token-processing-embeddings-and-classification.txt) | Apple | Single report | medium |
| [Query Image Similarity Search](../1p3a-qbanks/apple/raw/query-image-similarity-search.txt) | Apple | Single report | medium |
| [UNet Debugging for Image MLE](../1p3a-qbanks/apple/raw/unet-debugging-for-image-mle.txt) | Apple | Single report | medium |
| [CodeSignal ML / Research Scientist OA (10-Problem Battery)](../1p3a-qbanks/bytedance/raw/codesignal-ml-rs-oa.txt) | ByteDance / TikTok | Single report | hard |
| [DS Role-Play: Explain Model Regression to PM](../1p3a-qbanks/capitalone/raw/ds-role-play-pm-explanation.txt) | Capital One | Single report | – |
| [ML Coding: Linear Regression from Scratch](../1p3a-qbanks/capitalone/raw/ml-coding-linear-regression.txt) | Capital One | Single report | medium |
| [ML Coding: Top-P (Nucleus) Sampling](../1p3a-qbanks/capitalone/raw/ml-coding-top-p-sampling.txt) | Capital One | Single report | medium |
| [EQR Alpha-Factor Research Deep-Dive + LLM Inference Stability](../1p3a-qbanks/citadel/raw/eqr-alpha-factor-deep-dive.txt) | Citadel / Citadel Securities | Single report | hard |
| [MLE Onsite — Domain Round (ML Coding + SQL Window Function)](../1p3a-qbanks/coinbase/raw/mle-domain-sql-window.txt) | Coinbase | Single report | medium |
| [MLE Onsite — Jupyter Pair Programming on Messy Classification Data](../1p3a-qbanks/coinbase/raw/mle-jupyter-classification.txt) | Coinbase | Single report | medium |
| [1-D Valid Convolution with Multithreading Follow-up](../1p3a-qbanks/ibm/raw/one-dimensional-valid-convolution.txt) | IBM | Single report | medium |
| [Weather Data Energy-Usage Ranking](../1p3a-qbanks/intuit/raw/weather-data-energy-usage-ranking.txt) | Intuit | Single report | medium |
| [Logistic Regression Debugging](../1p3a-qbanks/linkedin/raw/coding-logistic-regression-debugging.txt) | LinkedIn | Single report | hard |
| [Recommender Training Pipeline](../1p3a-qbanks/linkedin/raw/coding-recommender-training-pipeline.txt) | LinkedIn | Single report | – |
| [One-Hot Encoder + ML Error Diagnosis](../1p3a-qbanks/lyft/raw/one-hot-encoder-and-ml-error-diagnosis.txt) | Lyft | Single report | medium |
| [Attention Implementation + Flash / Linear Attention Follow-ups](../1p3a-qbanks/meta/raw/attention-implementation-and-follow-ups.txt) | Meta | Single report | medium |
| [ML Foundations — Optimizers, Scaling Laws, K-Means and GMM](../1p3a-qbanks/meta/raw/ml-foundations-optimizers-scaling-law-clustering.txt) | Meta | Single report | medium |
| [SFT Sample Packing](../1p3a-qbanks/microsoft/raw/sft-sample-packing.txt) | Microsoft | Single report | hard |
| [Tiny Next-Token Prediction Model (PyTorch Fill-In)](../1p3a-qbanks/microsoft/raw/tiny-next-token-pytorch.txt) | Microsoft | Single report | medium |
| [Transformer Roofline + FP32→FP16 Drill](../1p3a-qbanks/microsoft/raw/transformer-roofline-fp-precision.txt) | Microsoft | Single report | hard |
| [FP32 Tensor to Int8 Quantization](../1p3a-qbanks/nvidia/raw/fp32-to-int8-quantization.txt) | NVIDIA | Single report | medium |
| [Cross-Entropy Loss Implementation](../1p3a-qbanks/openai/raw/masked-cross-entropy-loss.txt) | OpenAI | Single report | medium |
| [Evaluate Subjective, Nondeterministic Agent Outputs](../1p3a-qbanks/oracle/raw/evaluate-subjective-agent-outputs.txt) | Oracle | Single report | – |
| [ML Coding — Handwritten MHA and Sparse Matmul](../1p3a-qbanks/oracle/raw/ml-coding-mha-sparse-matmul.txt) | Oracle | Single report | medium |
| [Map-Reduce and Blockwise Attention](../1p3a-qbanks/perplexity/raw/map-reduce-and-blockwise-attention.txt) | Perplexity | Single report | hard |
| [scikit-learn Binary Classifier](../1p3a-qbanks/perplexity/raw/sklearn-binary-classifier.txt) | Perplexity | Single report | medium |
| [AI / ML Fundamentals Oral Round (AI Engineer)](../1p3a-qbanks/salesforce/raw/ai-ml-fundamentals-oral.txt) | Salesforce | Single report | medium |
| [Tabular Data Neural Network](../1p3a-qbanks/stripe/raw/tabular-data-neural-network.txt) | Stripe | Single report | medium |
| [Attention / Transformer Module with Backward Pass](../1p3a-qbanks/tesla/raw/autoregressive-attention-transformer-backward.txt) | Tesla | Single report | hard |
| [Batched Trajectory Suffix Sum](../1p3a-qbanks/tesla/raw/trajectory-suffix-sum.txt) | Tesla | Single report | medium |
| [CV / Sensing ML Fundamentals Oral](../1p3a-qbanks/tesla/raw/cv-sensing-ml-fundamentals-oral.txt) | Tesla | Single report | medium |
| [NumPy Point-to-Plane Distance and Robust Plane Fitting](../1p3a-qbanks/tesla/raw/numpy-point-plane-distance-robust-fitting.txt) | Tesla | Single report | medium |
| [Speed-Limit RL Reward from Trajectory Samples](../1p3a-qbanks/tesla/raw/speed-limit-rl-reward.txt) | Tesla | Single report | medium |
| [Scientist Onsite: Experiment Design (Switchback + CI Interpretation)](../1p3a-qbanks/uber/raw/scientist-experiment-design.txt) | Uber | Single report | – |
| [ML Coding: Debug a NumPy / Tensor Framework](../1p3a-qbanks/waymo/raw/ml-coding-tensor-framework-debug.txt) | Waymo | Single report | medium |
| [Agentic Workflow for 1-Hour Movie Generation](../1p3a-qbanks/xai/raw/agentic-one-hour-movie-generation.txt) | xAI | Single report | medium |
| [Distributed Matrix Multiplication — DP and FSDP](../1p3a-qbanks/xai/raw/distributed-matmul-dp-fsdp.txt) | xAI | Single report | hard |
| [Mock-LLM Inference Engine — Dynamic Batching](../1p3a-qbanks/xai/raw/mock-llm-dynamic-batching.txt) | xAI | Single report | hard |

</details>

<details><summary><b>intervals-scheduling</b> (87)</summary>

| Question | Company | Freq | Level |
|---|---|---|---|
| [Code Craft: Dasher Pay](../1p3a-qbanks/doordash/raw/code-craft-dasher-pay.txt) | DoorDash | Very high | medium |
| [Text Highlighting / Citation Tagging](../1p3a-qbanks/harvey/raw/text-highlighting-citation.txt) | Harvey | Very high | medium |
| [IP / CIDR Firewall](../1p3a-qbanks/databricks/raw/ip-cidr-firewall.txt) | Databricks | High | medium |
| [Meeting Rooms / Interval Scheduling Variants](../1p3a-qbanks/google/raw/meeting-rooms-variants.txt) | Google | High | medium |
| [Data Labeling Task Scheduler](../1p3a-qbanks/openai/raw/data-labeling-task-scheduler.txt) | OpenAI | High | medium |
| [Delivery Billing System](../1p3a-qbanks/rippling/raw/delivery-billing-system.txt) | Rippling | High | hard |
| [Progressive Banking System OA](../1p3a-qbanks/airbnb/raw/oa-banking-system.txt) | Airbnb | Medium | hard |
| [Expanding Tennis Club Court Assignment](../1p3a-qbanks/atlassian/raw/expanding-tennis-club-court-assignment.txt) | Atlassian | Medium | medium |
| [Merge Intervals](../1p3a-qbanks/bytedance/raw/merge-intervals.txt) | ByteDance / TikTok | Medium | medium |
| [Shard Rebalance / Overlapping Key Range](../1p3a-qbanks/openai/raw/shard-rebalance.txt) | OpenAI | Medium | medium |
| [Party Time Blocks](../1p3a-qbanks/scale.ai/raw/party-time-blocks.txt) | Scale AI | Medium | medium |
| [Email Subscription Scheduler](../1p3a-qbanks/stripe/raw/email-subscription-scheduler.txt) | Stripe | Medium | medium |
| [Incident Monitor — Transaction Alert Trigger / Resolve](../1p3a-qbanks/stripe/raw/transaction-logs-trigger-resolve.txt) | Stripe | Medium | medium |
| [Phone Screen / Onsite: Meeting Room Scheduler](../1p3a-qbanks/uber/raw/phone-screen-meeting-scheduler-rooms.txt) | Uber | Medium | medium |
| [Maximum Profit in Job Scheduling](../1p3a-qbanks/airbnb/raw/max-profit-job-scheduling.txt) | Airbnb | Low | medium |
| [Working Hours Register](../1p3a-qbanks/airbnb/raw/oa-working-hours-register.txt) | Airbnb | Low | hard |
| [Product Quality Values into Contiguous Blocks](../1p3a-qbanks/amazon/raw/product-quality-contiguous-blocks.txt) | Amazon | Low | medium |
| [OA — Worker / Employee Grant Management](../1p3a-qbanks/anthropic/raw/oa-worker-management.txt) | Anthropic | Low | medium |
| [Frontend React Screen](../1p3a-qbanks/apple/raw/frontend-react-screen.txt) | Apple | Low | medium |
| [Warehouse Round-Robin Allocation with Closures](../1p3a-qbanks/capitalone/raw/warehouse-round-robin-allocation.txt) | Capital One | Low | medium |
| [Task Scheduler with Priority + Lazy Deletion](../1p3a-qbanks/citadel/raw/task-scheduler-priority-lazy-delete.txt) | Citadel / Citadel Securities | Low | medium |
| [Cipher Cover Interval Editing](../1p3a-qbanks/databricks/raw/cipher-cover-interval-editing.txt) | Databricks | Low | medium |
| [Code Craft: Dasher Pay via Mock Service API](../1p3a-qbanks/doordash/raw/code-craft-dasher-pay-via-api-mock.txt) | DoorDash | Low | medium |
| [Maximum Concurrent Processes / Meeting Rooms](../1p3a-qbanks/ibm/raw/maximum-concurrent-processes.txt) | IBM | Low | medium |
| [Process Execution Time (Merge Inclusive Intervals)](../1p3a-qbanks/ibm/raw/process-execution-time-merge-intervals.txt) | IBM | Low | medium |
| [Customer Scheduler: VIP Priority and 2:1 Fairness](../1p3a-qbanks/intuit/raw/customer-scheduler-vip-fairness.txt) | Intuit | Low | easy |
| [Minimum CPU Cores](../1p3a-qbanks/jpmorgan/raw/minimum-cpu-cores.txt) | JPMorgan Chase | Low | easy |
| [Alert Monitor — Rolling Window + Severity Distribution + Spike Detection](../1p3a-qbanks/linkedin/raw/coding-alert-monitor.txt) | LinkedIn | Low | medium |
| [Job Scheduler: Minimum Workers with Assignment History](../1p3a-qbanks/lyft/raw/job-scheduler-minimum-workers.txt) | Lyft | Low | medium |
| [Dinosaur Speed (Meta PE classic)](../1p3a-qbanks/meta/raw/dinosaur-speed-meta-pe.txt) | Meta | Low | medium |
| [Merge Intervals (LC 56) + Interview Variant](../1p3a-qbanks/microsoft/raw/merge-intervals.txt) | Microsoft | Low | medium |
| [OptiCargo — Cargo-to-Flight Scheduling](../1p3a-qbanks/optiver/raw/opticargo-flight-scheduling.txt) | Optiver | Low | hard |
| [Elevator Assignment / Nearest Available Elevator](../1p3a-qbanks/pinterest/raw/elevator-assignment-nearest-available.txt) | Pinterest | Low | medium |
| [Policy Violation Records Aggregation](../1p3a-qbanks/pinterest/raw/policy-violation-records-aggregation.txt) | Pinterest | Low | medium |
| [Max Credits with K Classes (Interval-Style)](../1p3a-qbanks/snowflake/raw/course-credit-max-k-classes.txt) | Snowflake | Low | medium |
| [Account Scheduler with LRU](../1p3a-qbanks/stripe/raw/account-scheduler-lru.txt) | Stripe | Low | medium |
| [Deployment Window Scheduler (OA)](../1p3a-qbanks/stripe/raw/deployment-window-scheduler-oa.txt) | Stripe | Low | medium |
| [Jupyter / WebSocket Load Balancer (OA)](../1p3a-qbanks/stripe/raw/jupyter-load-balancer-oa.txt) | Stripe | Low | medium |
| [Streaming Interval Coverage on a Number Axis](../1p3a-qbanks/waymo/raw/interval-pollution-coverage.txt) | Waymo | Low | medium |
| [Task Management System OA](../1p3a-qbanks/airbnb/raw/oa-task-management-system.txt) | Airbnb | Single report | hard |
| [Maximum Concurrent Processes from Intervals](../1p3a-qbanks/amazon/raw/max-concurrent-processes-sweep-line.txt) | Amazon | Single report | medium |
| [Bold Words in String](../1p3a-qbanks/apple/raw/bold-words-in-string.txt) | Apple | Single report | medium |
| [Insert Interval (LC 57)](../1p3a-qbanks/apple/raw/insert-interval.txt) | Apple | Single report | medium |
| [Merge Intervals](../1p3a-qbanks/apple/raw/merge-intervals.txt) | Apple | Single report | medium |
| [Meeting Rooms II](../1p3a-qbanks/bloomberg/raw/meeting-rooms-ii.txt) | Bloomberg | Single report | medium |
| [Merge Intervals (LC 56)](../1p3a-qbanks/bloomberg/raw/merge-intervals.txt) | Bloomberg | Single report | medium |
| [Intersection of Two Interval Lists](../1p3a-qbanks/bytedance/raw/intersection-of-two-interval-lists.txt) | ByteDance / TikTok | Single report | medium |
| [Non-Overlapping Intervals (LeetCode 435)](../1p3a-qbanks/bytedance/raw/non-overlapping-intervals.txt) | ByteDance / TikTok | Single report | – |
| [Seller Task Scheduler](../1p3a-qbanks/bytedance/raw/seller-task-scheduler.txt) | ByteDance / TikTok | Single report | – |
| [Timestamped Task Scheduler](../1p3a-qbanks/bytedance/raw/timestamped-task-scheduler.txt) | ByteDance / TikTok | Single report | medium |
| [Battery Usage with Recharge Cycle](../1p3a-qbanks/capitalone/raw/battery-recharge-cycle.txt) | Capital One | Single report | medium |
| [House Segments After Deletions](../1p3a-qbanks/capitalone/raw/house-segments-after-deletions.txt) | Capital One | Single report | medium |
| [Compress Consecutive Ranges](../1p3a-qbanks/cisco/raw/compress-consecutive-ranges.txt) | Cisco | Single report | easy |
| [Implement a Round-Robin Task Scheduler](../1p3a-qbanks/citadel/raw/round-robin-task-scheduler.txt) | Citadel / Citadel Securities | Single report | medium |
| [Largest Team Whose Members Share a Common Office Window](../1p3a-qbanks/citadel/raw/interval-overlap-max-team.txt) | Citadel / Citadel Securities | Single report | medium |
| [Code Craft: Batch with Capacity & Time Window (MLE)](../1p3a-qbanks/doordash/raw/code-craft-batch-with-capacity-window-mle.txt) | DoorDash | Single report | medium |
| [Union of Sorted Interval Lists](../1p3a-qbanks/google/raw/sorted-interval-list-union.txt) | Google | Single report | medium |
| [OA: Task Management System](../1p3a-qbanks/instacart/raw/oa-task-management-system.txt) | Instacart | Single report | hard |
| [OA: Worker Attendance & Payroll](../1p3a-qbanks/instacart/raw/oa-worker-attendance-payroll.txt) | Instacart | Single report | hard |
| [Interval Consolidation OA](../1p3a-qbanks/jpmorgan/raw/interval-consolidation-oa.txt) | JPMorgan Chase | Single report | medium |
| [Interval Sign Flips](../1p3a-qbanks/jpmorgan/raw/interval-sign-flips.txt) | JPMorgan Chase | Single report | easy |
| [Meeting Rooms (Conflict Check)](../1p3a-qbanks/netflix/raw/meeting-rooms-conflict-check.txt) | Netflix | Single report | easy |
| [Merge Intervals](../1p3a-qbanks/netflix/raw/merge-intervals.txt) | Netflix | Single report | medium |
| [OOD — Treatment Frequency Scheduler](../1p3a-qbanks/oracle/raw/ood-treatment-scheduler.txt) | Oracle | Single report | medium |
| [Credit Tracker with Expiring Credits](../1p3a-qbanks/perplexity/raw/credit-tracker-with-expiring-credits.txt) | Perplexity | Single report | medium |
| [File Chunk Range Tracking](../1p3a-qbanks/roblox/raw/file-chunks-consecutive-byte-ranges.txt) | Roblox | Single report | – |
| [Meeting Rooms II (LeetCode 253)](../1p3a-qbanks/roblox/raw/meeting-rooms-ii.txt) | Roblox | Single report | medium |
| [Merge Intervals (LeetCode 56)](../1p3a-qbanks/roblox/raw/merge-intervals.txt) | Roblox | Single report | medium |
| [Phone Battery Discharge Scheduling](../1p3a-qbanks/roblox/raw/phone-battery-discharge-scheduling.txt) | Roblox | Single report | medium |
| [Single-Threaded CPU (LeetCode 1834)](../1p3a-qbanks/roblox/raw/single-threaded-cpu.txt) | Roblox | Single report | medium |
| [Maximum Profit in Job Scheduling](../1p3a-qbanks/snowflake/raw/maximum-profit-in-job-scheduling.txt) | Snowflake | Single report | hard |
| [Meeting Rooms II](../1p3a-qbanks/snowflake/raw/meeting-rooms-ii.txt) | Snowflake | Single report | medium |
| [Merge Intervals](../1p3a-qbanks/snowflake/raw/merge-intervals.txt) | Snowflake | Single report | medium |
| [Priority Task Executor](../1p3a-qbanks/snowflake/raw/priority-task-executor.txt) | Snowflake | Single report | medium |
| [Shipping Cost Calculator](../1p3a-qbanks/stripe/raw/shipping-cost-calculator.txt) | Stripe | Single report | medium |
| [Time Slot Generator from Weekly Working Hours](../1p3a-qbanks/stripe/raw/time-slot-generator.txt) | Stripe | Single report | easy |
| [Worker Task Assignment](../1p3a-qbanks/stripe/raw/worker-task-assignment.txt) | Stripe | Single report | medium |
| [Palindrome Warmup + Two-Task Worker Scheduling](../1p3a-qbanks/twosigma/raw/palindrome-and-task-scheduling-round.txt) | Two Sigma | Single report | medium |
| [Active Couriers Timeline](../1p3a-qbanks/uber/raw/active-couriers-timeline.txt) | Uber | Single report | medium |
| [Interval List Intersections](../1p3a-qbanks/uber/raw/interval-list-intersections.txt) | Uber | Single report | medium |
| [My Calendar I](../1p3a-qbanks/uber/raw/my-calendar-i.txt) | Uber | Single report | medium |
| [Test Run Status Monitor (Failing → Passing Windows)](../1p3a-qbanks/vanta/raw/test-status-monitor.txt) | Vanta | Single report | hard |
| [Merge Intervals Returning Original Start/End Indices](../1p3a-qbanks/walmartlabs/raw/merge-intervals-with-indices.txt) | Walmart Labs | Single report | medium |
| [Range Module (LC 715)](../1p3a-qbanks/walmartlabs/raw/range-module-lc-715.txt) | Walmart Labs | Single report | hard |
| [OO Design: Continuous Time Intervals from Two Timestamp Signal Streams](../1p3a-qbanks/waymo/raw/timestamp-signals-time-intervals-ood.txt) | Waymo | Single report | medium |
| [Waymo Passenger Pickup Scheduler (OO Design)](../1p3a-qbanks/waymo/raw/passenger-pickup-scheduler-oo-design.txt) | Waymo | Single report | medium |
| [Twitter Spaces — Aggregate Active Hours](../1p3a-qbanks/xai/raw/twitter-spaces-active-hours.txt) | xAI | Single report | medium |

</details>

<details><summary><b>heap-topk</b> (76)</summary>

| Question | Company | Freq | Level |
|---|---|---|---|
| [Rolling Statistics: Max, Mean, and Mode](../1p3a-qbanks/akunacapital/raw/rolling-statistics-max-mean-mode.txt) | Akuna Capital | Medium | medium |
| [Agent Rating Average Ranking](../1p3a-qbanks/atlassian/raw/agent-rating-average-ranking.txt) | Atlassian | Medium | medium |
| [Banking System (Multi-Level OA)](../1p3a-qbanks/coinbase/raw/banking-system-oa.txt) | Coinbase | Medium | medium |
| [Top-K Frequent in Large Logs](../1p3a-qbanks/google/raw/top-k-frequent-from-logs.txt) | Google | Medium | medium |
| [Kth Largest Element / Quick Select](../1p3a-qbanks/meta/raw/kth-largest-quick-select.txt) | Meta | Medium | medium |
| [News Aggregation / Subscription System](../1p3a-qbanks/optiver/raw/news-subscription-system.txt) | Optiver | Medium | hard |
| [Order Book Matching Simulation](../1p3a-qbanks/optiver/raw/order-book-matching.txt) | Optiver | Medium | medium |
| [Article Voting / Top-K](../1p3a-qbanks/rippling/raw/article-voting-system.txt) | Rippling | Medium | medium |
| [Track Best Bid/Ask with Cancellations](../1p3a-qbanks/akunacapital/raw/track-best-bid-ask-with-cancels.txt) | Akuna Capital | Low | medium |
| [Catalog Top-K / Kth-Largest Selection](../1p3a-qbanks/amazon/raw/catalog-top-k-selection.txt) | Amazon | Low | medium |
| [Find Median from Data Stream (LC 295)](../1p3a-qbanks/amazon/raw/find-median-data-stream-lc-295.txt) | Amazon | Low | hard |
| [Top K Frequent Elements (LC 347) with a Follow-Up Variant](../1p3a-qbanks/amazon/raw/top-k-frequent-elements-streaming-follow-up.txt) | Amazon | Low | medium |
| [Commodity Price Checkpoints](../1p3a-qbanks/atlassian/raw/commodity-price-checkpoints.txt) | Atlassian | Low | medium |
| [Top-K Frequent Tickers (Stock Leaderboard)](../1p3a-qbanks/bloomberg/raw/top-k-frequent-stock-tickers.txt) | Bloomberg | Low | medium |
| [MinStack, MaxStack and Streaming Median](../1p3a-qbanks/bytedance/raw/streaming-median-min-max-stack.txt) | ByteDance / TikTok | Low | hard |
| [Merge K Sorted Streams (OOD + Algorithm)](../1p3a-qbanks/citadel/raw/merge-k-sorted-streams-ood.txt) | Citadel / Citadel Securities | Low | medium |
| [Merge K Sorted Lists (incl. K = 3)](../1p3a-qbanks/goldmansachs/raw/merge-k-sorted-lists.txt) | Goldman Sachs | Low | medium |
| [Huffman Tree Construction](../1p3a-qbanks/google/raw/huffman-tree-construction.txt) | Google | Low | hard |
| [Minimum Removal Rounds by Smallest Response Time](../1p3a-qbanks/ibm/raw/minimum-removal-rounds-by-smallest-response-time.txt) | IBM | Low | medium |
| [VM Rental Revenue from Changing Stock](../1p3a-qbanks/ibm/raw/vm-rental-revenue.txt) | IBM | Low | easy |
| [Top-K Largest Elements (Retain / Rank Stores)](../1p3a-qbanks/microsoft/raw/top-k-largest-retain.txt) | Microsoft | Low | easy |
| [Movie Billboard Rotation Service](../1p3a-qbanks/netflix/raw/movie-billboard-rotation-service.txt) | Netflix | Low | medium |
| [Byte Tokenizer and Token-Count Estimator](../1p3a-qbanks/perplexity/raw/byte-tokenizer-and-token-count-estimator.txt) | Perplexity | Low | hard |
| [Stack Items into the Shortest Column (Min-Heap Load Balancing)](../1p3a-qbanks/pinterest/raw/shortest-column-stacking-heap.txt) | Pinterest | Low | medium |
| [Music Player OOD](../1p3a-qbanks/rippling/raw/music-player-ood.txt) | Rippling | Low | medium |
| [Second Most Frequent GIF Tag](../1p3a-qbanks/sofi/raw/second-most-frequent-gif-tag.txt) | SoFi | Low | medium |
| [Order Matching Engine](../1p3a-qbanks/twosigma/raw/order-matching-engine.txt) | Two Sigma | Low | medium |
| [Transaction Balance & Minimum Settlement](../1p3a-qbanks/affirm/raw/transaction-balance-min-settlement.txt) | Affirm | Single report | medium |
| [Lexicographically Largest String via State Flips](../1p3a-qbanks/amazon/raw/lex-largest-string-via-state-flips.txt) | Amazon | Single report | hard |
| [Music Player with Frequency Priority](../1p3a-qbanks/amazon/raw/music-player-frequency-priority.txt) | Amazon | Single report | medium |
| [Reorganize a String with No Equal Neighbors](../1p3a-qbanks/amazon/raw/reorganize-string-adjacent-different.txt) | Amazon | Single report | medium |
| [VM Inventory Rental — Heap Simulation](../1p3a-qbanks/amazon/raw/vm-inventory-rental-heap.txt) | Amazon | Single report | medium |
| [Failed Login Log Ranking](../1p3a-qbanks/apple/raw/failed-login-log-ranking.txt) | Apple | Single report | easy |
| [Find K Pairs with Smallest Sums](../1p3a-qbanks/apple/raw/find-k-pairs-with-smallest-sums.txt) | Apple | Single report | medium |
| [Kth Largest Element in an Array](../1p3a-qbanks/apple/raw/kth-largest-element-in-an-array.txt) | Apple | Single report | medium |
| [Maximum Apples Before Rot](../1p3a-qbanks/apple/raw/maximum-apples-before-rot.txt) | Apple | Single report | medium |
| [Merge K Sorted Lists](../1p3a-qbanks/apple/raw/merge-k-sorted-lists.txt) | Apple | Single report | medium |
| [Top K Closest Pairs in a Sorted Array](../1p3a-qbanks/apple/raw/top-k-closest-pairs-in-sorted-array.txt) | Apple | Single report | medium |
| [Top K Frequent Elements](../1p3a-qbanks/apple/raw/top-k-frequent-elements.txt) | Apple | Single report | medium |
| [Citadel QD On-Site Coding Ladder (3 Rounds, 3-4 Problems Each)](../1p3a-qbanks/citadel/raw/qd-eqr-onsite-coding-ladder.txt) | Citadel / Citadel Securities | Single report | medium |
| [GQS SWE Whiteboard: Boundary Search + Sliding-Window Top K](../1p3a-qbanks/citadel/raw/gqs-swe-whiteboard-boundary-search-top-k.txt) | Citadel / Citadel Securities | Single report | medium |
| [Task Management System OA](../1p3a-qbanks/coinbase/raw/task-management-oa.txt) | Coinbase | Single report | medium |
| [Extend Queue: Min-Size & Min-Sum Selector](../1p3a-qbanks/goldmansachs/raw/queue-extension-min-size-min-sum.txt) | Goldman Sachs | Single report | medium |
| [Active Communication Top Users](../1p3a-qbanks/google/raw/active-communication-top-users.txt) | Google | Single report | medium |
| [Batch and Streaming Log Deduplication](../1p3a-qbanks/google/raw/batch-streaming-log-deduplication.txt) | Google | Single report | medium |
| [Streaming Median (LC 295) with Follow-ups](../1p3a-qbanks/google/raw/streaming-median-with-follow-ups.txt) | Google | Single report | medium |
| [Cache with Rank-Based Eviction Policy](../1p3a-qbanks/linkedin/raw/coding-cache-rank-eviction.txt) | LinkedIn | Single report | medium |
| [Find K Closest Elements in a Sorted Array (LC 658)](../1p3a-qbanks/linkedin/raw/coding-k-closest-elements-sorted-array.txt) | LinkedIn | Single report | medium |
| [Minimum-Cost Array Reduction](../1p3a-qbanks/mathworks/raw/minimum-cost-array-reduction.txt) | MathWorks | Single report | easy |
| [Beam Search Decoder Implementation](../1p3a-qbanks/microsoft/raw/beam-search-decoder.txt) | Microsoft | Single report | hard |
| [Systems Utility Coding: Temperature Spike, Logs, Brackets](../1p3a-qbanks/nvidia/raw/systems-utility-coding.txt) | NVIDIA | Single report | easy |
| [Sort Shows by User Preference](../1p3a-qbanks/netflix/raw/sort-shows-by-user-preference.txt) | Netflix | Single report | – |
| [Request Rate Limiter / Sliding Window](../1p3a-qbanks/okta/raw/rate-limiter.txt) | Okta | Single report | medium |
| [Merge K Sorted (Key, Value) Lists with Later Override](../1p3a-qbanks/oracle/raw/merge-k-sorted-key-value-lists.txt) | Oracle | Single report | medium |
| [Minimum Sum After K Halvings (Max-Heap)](../1p3a-qbanks/oracle/raw/min-sum-after-k-halvings.txt) | Oracle | Single report | medium |
| [Beam Search Decoder](../1p3a-qbanks/perplexity/raw/beam-search-decoder.txt) | Perplexity | Single report | medium |
| [Top-K Ads from Log + Sliding-Window Ingest](../1p3a-qbanks/pinterest/raw/top-k-ads-from-log-sliding-window.txt) | Pinterest | Single report | medium |
| [Design Search Autocomplete System](../1p3a-qbanks/roblox/raw/design-search-autocomplete-system.txt) | Roblox | Single report | hard |
| [Highest-Earning Experience Tracker](../1p3a-qbanks/roblox/raw/highest-earning-experience-tracker.txt) | Roblox | Single report | medium |
| [Search Suggestion Service](../1p3a-qbanks/snapchat/raw/search-suggestion-service.txt) | Snapchat | Single report | hard |
| [Smallest Range Covering Elements from K Lists (LC 632)](../1p3a-qbanks/snapchat/raw/smallest-range-covering-elements-from-k-lists.txt) | Snapchat | Single report | hard |
| [Find Median from Data Stream](../1p3a-qbanks/snowflake/raw/find-median-from-data-stream.txt) | Snowflake | Single report | hard |
| [K Top Selling Books](../1p3a-qbanks/snowflake/raw/k-top-selling-books.txt) | Snowflake | Single report | medium |
| [Maximum Number of Events That Can Be Attended](../1p3a-qbanks/snowflake/raw/maximum-events-attend-greedy.txt) | Snowflake | Single report | medium |
| [Merge K Sorted Lists](../1p3a-qbanks/snowflake/raw/merge-k-sorted-lists.txt) | Snowflake | Single report | hard |
| [Top-K Search Terms (Per-User Deduped)](../1p3a-qbanks/snowflake/raw/top-k-search-terms-deduped.txt) | Snowflake | Single report | medium |
| [Maximum Positive Prefixes](../1p3a-qbanks/sofi/raw/maximum-positive-prefixes.txt) | SoFi | Single report | medium |
| [Top K Frequent Elements (LC 347) with a Follow-Up Variant](../1p3a-qbanks/pinduoduo/raw/top-k-frequent-elements.txt) | Temu / Pinduoduo | Single report | medium |
| [Twitter Feed API Implementation](../1p3a-qbanks/pinduoduo/raw/twitter-feed-api-implementation.txt) | Temu / Pinduoduo | Single report | medium |
| [Priority + Expiration + LRU Eviction](../1p3a-qbanks/tesla/raw/priority-expire-cache-eviction.txt) | Tesla | Single report | medium |
| [Huffman-Style Binary Encode / Decode](../1p3a-qbanks/twosigma/raw/huffman-style-binary-encode-decode.txt) | Two Sigma | Single report | hard |
| [Customer Revenue and Referral Tracking](../1p3a-qbanks/uber/raw/customer-revenue-referral-tracking.txt) | Uber | Single report | medium |
| [Onsite Coding: K Closest Points](../1p3a-qbanks/uber/raw/onsite-k-closest-points.txt) | Uber | Single report | medium |
| [Seat Assignment / Exam Room](../1p3a-qbanks/uber/raw/exam-room-seat-assignment.txt) | Uber | Single report | medium |
| [Top K Frequent Elements](../1p3a-qbanks/uber/raw/top-k-frequent-elements.txt) | Uber | Single report | medium |
| [Remove m Elements to Minimize Unique Count](../1p3a-qbanks/walmartlabs/raw/remove-m-minimize-unique-elements.txt) | Walmart Labs | Single report | medium |

</details>

<details><summary><b>behavioral</b> (72)</summary>

| Question | Company | Freq | Level |
|---|---|---|---|
| [BQ — GenAI Usage (How / Failure / Tradeoff)](../1p3a-qbanks/amazon/raw/bq-genai-usage.txt) | Amazon | Very high | – |
| [BQ — Leadership Principles Standard Set](../1p3a-qbanks/amazon/raw/bq-leadership-principles-standard-set.txt) | Amazon | Very high | – |
| [Culture Round — AI-Safety Alignment Behavioral](../1p3a-qbanks/anthropic/raw/onsite-culture-ai-safety.txt) | Anthropic | Very high | – |
| [Hiring Manager Round — Behavioral & Collaboration](../1p3a-qbanks/anthropic/raw/onsite-hm-behavioral.txt) | Anthropic | Very high | – |
| [Project Deep-Dive — Technical Presentation](../1p3a-qbanks/anthropic/raw/onsite-project-deep-dive.txt) | Anthropic | Very high | – |
| [Recruiter Screen — Why Anthropic Deep-Dive](../1p3a-qbanks/anthropic/raw/recruiter-screen-why-anthropic.txt) | Anthropic | Very high | – |
| [Project Deep Dive (Resume Drill)](../1p3a-qbanks/bytedance/raw/project-deep-dive-round.txt) | ByteDance / TikTok | Very high | medium |
| [Hiring Manager Behavioral Round](../1p3a-qbanks/doordash/raw/hm-behavioral-round.txt) | DoorDash | Very high | easy |
| [Hiring Manager & Behavioral Rounds](../1p3a-qbanks/figma/raw/hiring-manager-behavioral.txt) | Figma | Very high | – |
| [Behavioral Round (Googliness Themes)](../1p3a-qbanks/google/raw/bq-round-google-style.txt) | Google | Very high | – |
| [Behavioral Round (Growth Mindset / Customer Obsession)](../1p3a-qbanks/microsoft/raw/behavioral-round.txt) | Microsoft | Very high | – |
| [HM BQ — Why OpenAI / AI Safety / AGI](../1p3a-qbanks/openai/raw/hm-bq-why-openai.txt) | OpenAI | Very high | – |
| [Behavioral & Bar Raiser BQ Set](../1p3a-qbanks/oracle/raw/behavioral-bq-bar-raiser.txt) | Oracle | Very high | – |
| [Project Deep Dive & Behavioral](../1p3a-qbanks/pinduoduo/raw/project-deep-dive-and-bq.txt) | Temu / Pinduoduo | Very high | – |
| [Onsite BQ + Hiring Manager + Project Deep-Dive](../1p3a-qbanks/uber/raw/onsite-bq-hm-deep-dive.txt) | Uber | Very high | – |
| [Hiring Manager & Behavioral Rounds](../1p3a-qbanks/affirm/raw/hiring-manager-behavioral.txt) | Affirm | High | – |
| [Core Values Behavioral Round](../1p3a-qbanks/airbnb/raw/core-values-bq-round.txt) | Airbnb | High | medium |
| [Experience / Project Deep-Dive Round](../1p3a-qbanks/airbnb/raw/experience-project-deep-dive.txt) | Airbnb | High | medium |
| [Work Simulation & Work Style Assessment (OA)](../1p3a-qbanks/amazon/raw/work-style-simulation-assessment.txt) | Amazon | High | – |
| [Behavioral Prioritization and Stakeholder Fit](../1p3a-qbanks/apple/raw/behavioral-prioritization-and-stakeholder-fit.txt) | Apple | High | – |
| [Atlassian Values and Management Rounds](../1p3a-qbanks/atlassian/raw/atlassian-values-and-management-rounds.txt) | Atlassian | High | – |
| [Behavioral Question Pool](../1p3a-qbanks/bloomberg/raw/behavioral-question-pool.txt) | Bloomberg | High | easy |
| [Hiring Manager / Final Behavioral Round](../1p3a-qbanks/bytedance/raw/hm-behavioral-final-round.txt) | ByteDance / TikTok | High | medium |
| [Power Day Behavioral](../1p3a-qbanks/capitalone/raw/power-day-behavioral.txt) | Capital One | High | – |
| [Behavioral / Resume Drilling Across the Loop](../1p3a-qbanks/citadel/raw/citadel-bq-resume-loop.txt) | Citadel / Citadel Securities | High | – |
| [Hiring Manager — Project Deep-Dive + AI Usage BQ](../1p3a-qbanks/coinbase/raw/hm-bq-project-deep-dive.txt) | Coinbase | High | medium |
| [Harvey Values / Behavioral](../1p3a-qbanks/harvey/raw/harvey-values-behavioral.txt) | Harvey | High | – |
| [Super Day Behavioral Round](../1p3a-qbanks/jpmorgan/raw/superday-behavioral-round.txt) | JPMorgan Chase | High | medium |
| [Behavioral / BQ Question Pack (Staff & Senior)](../1p3a-qbanks/linkedin/raw/behavioral-bq-pack.txt) | LinkedIn | High | medium |
| [Project Deep-Dive (Four-Axis Rubric)](../1p3a-qbanks/linkedin/raw/behavioral-project-deep-dive.txt) | LinkedIn | High | medium |
| [AI-Oriented Behavioral (AI BQ, NEW)](../1p3a-qbanks/meta/raw/ai-oriented-behavioral.txt) | Meta | High | medium |
| [Traditional Behavioral — Meta](../1p3a-qbanks/meta/raw/traditional-behavioral-meta.txt) | Meta | High | medium |
| [Project Deep Dive](../1p3a-qbanks/microsoft/raw/project-deep-dive.txt) | Microsoft | High | – |
| [Netflix Culture / Domain Manager Rounds](../1p3a-qbanks/netflix/raw/netflix-culture-and-domain-manager-rounds.txt) | Netflix | High | – |
| [Recruiter / HR Screen (BQ)](../1p3a-qbanks/openai/raw/recruiter-hr-screen-bq.txt) | OpenAI | High | – |
| [Behavioral: Leadership, Initiative, Project End-to-End](../1p3a-qbanks/pinterest/raw/behavioral-leadership-initiative.txt) | Pinterest | High | medium |
| [Project Deep Dive / HM BQ](../1p3a-qbanks/rippling/raw/project-deep-dive-and-bq.txt) | Rippling | High | medium |
| [HM Behavioral: Traffic Scale & Scope of Past Work](../1p3a-qbanks/roblox/raw/behavioral-hm-traffic-and-scope.txt) | Roblox | High | medium |
| [Hiring Manager + Behavioral Rounds](../1p3a-qbanks/salesforce/raw/hm-and-bq-rounds.txt) | Salesforce | High | medium |
| [Credo Behavioral and Customer Engagement](../1p3a-qbanks/scale.ai/raw/credo-behavioral-and-customer-engagement.txt) | Scale AI | High | medium |
| [Life Story / Values Interview](../1p3a-qbanks/shopify/raw/life-story-values-interview.txt) | Shopify | High | – |
| [Hiring Manager Behavioral](../1p3a-qbanks/stripe/raw/hiring-manager-behavioral.txt) | Stripe | High | easy |
| [Principles & Leadership Behavioral Rounds](../1p3a-qbanks/vanta/raw/principles-and-leadership-bq.txt) | Vanta | High | – |
| [Most Challenging Project + Why xAI](../1p3a-qbanks/xai/raw/most-challenging-project-why-xai.txt) | xAI | High | easy |
| [Hiring Manager Screen](../1p3a-qbanks/airbnb/raw/hiring-manager-screen.txt) | Airbnb | Medium | easy |
| [SWE / MLE Behavioral Round](../1p3a-qbanks/databricks/raw/swe-behavioral-round.txt) | Databricks | Medium | – |
| [Project Deep Dive](../1p3a-qbanks/figma/raw/project-deep-dive.txt) | Figma | Medium | – |
| [Hirevue Behavioral Bank (6 Questions)](../1p3a-qbanks/goldmansachs/raw/hirevue-behavioral-six-questions.txt) | Goldman Sachs | Medium | easy |
| [Behavioral: Project, Conflict, Failure, and DEI](../1p3a-qbanks/lyft/raw/behavioral-project-conflict-and-dei.txt) | Lyft | Medium | medium |
| [EDG HireVue Screen](../1p3a-qbanks/mathworks/raw/edg-hirevue-screen.txt) | MathWorks | Medium | medium |
| [HR / Behavioral Phone Screen](../1p3a-qbanks/optiver/raw/hr-behavioral-screen.txt) | Optiver | Medium | – |
| [Behavioral and Project Deep-Dive](../1p3a-qbanks/perplexity/raw/behavioral-and-project-deep-dive.txt) | Perplexity | Medium | medium |
| [Hiring-Manager Domain Round](../1p3a-qbanks/reddit/raw/hiring-manager-domain-round.txt) | Reddit | Medium | medium |
| [Recruiter / HR Screen](../1p3a-qbanks/robinhood/raw/recruiter-hr-screen.txt) | Robinhood | Medium | – |
| [Manager Behavioral and Project Deep Dive](../1p3a-qbanks/sofi/raw/manager-behavioral-and-project-deep-dive.txt) | SoFi | Medium | – |
| [Walmart Behavioral / HM Loop](../1p3a-qbanks/walmartlabs/raw/walmart-bq-loop.txt) | Walmart Labs | Medium | – |
| [Motivation, Ambiguity, and Collaboration Screen](../1p3a-qbanks/bytedance/raw/motivation-ambiguity-collaboration-screen.txt) | ByteDance / TikTok | Low | medium |
| [Explain Recursion to a Non-CS Audience](../1p3a-qbanks/goldmansachs/raw/explain-recursion-no-cs-audience.txt) | Goldman Sachs | Low | easy |
| [Competency and Resume Interview](../1p3a-qbanks/ibm/raw/competency-and-resume-interview.txt) | IBM | Low | – |
| [Culture Fit, Motivation, and Learning Habits](../1p3a-qbanks/nvidia/raw/culture-fit-motivation-and-learning-habits.txt) | NVIDIA | Low | medium |
| [Hiring-Manager / Project Chat + AI Philosophy](../1p3a-qbanks/okta/raw/hm-project-and-ai-philosophy.txt) | Okta | Low | – |
| [AI-Conducted Screening Interview](../1p3a-qbanks/snowflake/raw/ai-conducted-screening.txt) | Snowflake | Low | medium |
| [Onsite Behavioral: Proud Project / Conflict / Failure](../1p3a-qbanks/snowflake/raw/onsite-behavioral-proud-conflict-failure.txt) | Snowflake | Low | medium |
| [Project Deep-Dive + ML Fundamentals Discussion](../1p3a-qbanks/waymo/raw/project-deep-dive-ml-fundamentals.txt) | Waymo | Low | medium |
| [Data Engineering Experience Screen](../1p3a-qbanks/bytedance/raw/data-engineering-experience-screen.txt) | ByteDance / TikTok | Single report | – |
| [Senior FDE Recruiter Screen](../1p3a-qbanks/databricks/raw/senior-fde-recruiter-screen.txt) | Databricks | Single report | – |
| [Engineering Management Fundamentals & Team Operations](../1p3a-qbanks/openai/raw/engineering-management-fundamentals.txt) | OpenAI | Single report | – |
| [Async Video Behavioral Screen](../1p3a-qbanks/ramp/raw/video-interview-behavioral.txt) | Ramp | Single report | – |
| [Infra Recruiter Screen: Experience and Tooling](../1p3a-qbanks/snowflake/raw/infra-recruiter-experience-screen.txt) | Snowflake | Single report | – |
| [Bar Raiser: Architecture, Influence, and Values](../1p3a-qbanks/uber/raw/bar-raiser-architecture-and-values.txt) | Uber | Single report | – |
| [Staff (BPS) Round: Scope & Impact + Architecture](../1p3a-qbanks/uber/raw/staff-bps-scope-architecture.txt) | Uber | Single report | – |
| [L5+ Behavioral: Policy vs Efficiency Trade-offs & Leadership](../1p3a-qbanks/waymo/raw/behavioral-policy-vs-efficiency-leadership.txt) | Waymo | Single report | hard |

</details>

<details><summary><b>dp-backtracking</b> (67)</summary>

| Question | Company | Freq | Level |
|---|---|---|---|
| [AI Coding — Maximum Unique Character Subset](../1p3a-qbanks/meta/raw/ai-coding-max-unique-character-subset.txt) | Meta | High | medium |
| [Stock Transaction Sequence Counting (Catalan / DP)](../1p3a-qbanks/optiver/raw/stock-transaction-counting.txt) | Optiver | High | medium |
| [OA: Jump Game with Prime-3 Steps](../1p3a-qbanks/uber/raw/oa-jump-game-prime.txt) | Uber | High | hard |
| [Smallest Permutation ≥ Lower Bound](../1p3a-qbanks/airbnb/raw/smallest-permutation-bound.txt) | Airbnb | Medium | medium |
| [Food Delivery System (Multi-Level Coding)](../1p3a-qbanks/coinbase/raw/food-delivery-system.txt) | Coinbase | Medium | medium |
| [AI Coding — Card Game (Three Cards Summing to 15)](../1p3a-qbanks/meta/raw/ai-coding-card-game-sum-fifteen.txt) | Meta | Medium | medium |
| [Optimal Execution with a Broker Backstop](../1p3a-qbanks/optiver/raw/optimal-execution-broker.txt) | Optiver | Medium | hard |
| [Word Guessing Game (Wordle-style)](../1p3a-qbanks/shopify/raw/word-guessing-game.txt) | Shopify | Medium | medium |
| [Layover Experiences — Exact-Fill with Min Count](../1p3a-qbanks/airbnb/raw/layover-experiences-exact-fill.txt) | Airbnb | Low | medium |
| [Menu — Minimum Cost to Cover Wanted Items](../1p3a-qbanks/airbnb/raw/menu-min-cost-bitmask.txt) | Airbnb | Low | medium |
| [Max Subsequence Sum Without Skipping Two in a Row](../1p3a-qbanks/akunacapital/raw/max-sum-no-two-consecutive-skips.txt) | Akuna Capital | Low | medium |
| [Morse Code Encoder / Decoder with Word-Break](../1p3a-qbanks/amazon/raw/morse-code-encoder-decoder.txt) | Amazon | Low | hard |
| [Longest Palindromic Substring (LeetCode 5)](../1p3a-qbanks/bytedance/raw/longest-palindromic-substring.txt) | ByteDance / TikTok | Low | medium |
| [Minimum Insertions / Deletions Password Update](../1p3a-qbanks/cisco/raw/minimum-insertions-deletions-password-update.txt) | Cisco | Low | medium |
| [Best Time to Buy and Sell Stock — Full DP Ladder](../1p3a-qbanks/citadel/raw/stock-trading-dp-ladder.txt) | Citadel / Citadel Securities | Low | medium |
| [Decode Ways / Alphanumeric Combination](../1p3a-qbanks/goldmansachs/raw/decode-ways.txt) | Goldman Sachs | Low | medium |
| [String Transformation Length](../1p3a-qbanks/mathworks/raw/string-transformation-length.txt) | MathWorks | Low | medium |
| [Expression Add Operators (LC 282)](../1p3a-qbanks/pinterest/raw/expression-add-operators-simplified.txt) | Pinterest | Low | medium |
| [Hyperparameter Combinations (Cartesian Product)](../1p3a-qbanks/pinterest/raw/hyperparameter-cartesian-product.txt) | Pinterest | Low | easy |
| [Longest Subsequence of X That Is a Substring of Y](../1p3a-qbanks/salesforce/raw/longest-subsequence-substring.txt) | Salesforce | Low | medium |
| [SWE / SDE Intern Coding OA (DP + C++/Ruby)](../1p3a-qbanks/shopify/raw/swe-intern-coding-oa.txt) | Shopify | Low | medium |
| [Best Time to Buy and Sell Stock](../1p3a-qbanks/uber/raw/best-time-buy-sell-stock.txt) | Uber | Low | easy |
| [Letter Combinations of a Phone Number](../1p3a-qbanks/uber/raw/letter-combinations-phone-number.txt) | Uber | Low | medium |
| [Property Combination for Group Size](../1p3a-qbanks/airbnb/raw/property-combination-min-capacity.txt) | Airbnb | Single report | medium |
| [Best Time to Buy & Sell Stock — Merged Variants](../1p3a-qbanks/amazon/raw/buy-sell-stock-merged-variants.txt) | Amazon | Single report | medium |
| [Bucket Batching DP (Applied Scientist Phone Screen)](../1p3a-qbanks/amazon/raw/bucket-batching-dp-applied-scientist.txt) | Amazon | Single report | hard |
| [Edit Distance (LC 72) with a Follow-Up Variant](../1p3a-qbanks/amazon/raw/edit-distance-one-reverse.txt) | Amazon | Single report | medium |
| [Min Errors in 0/1/! Subsequence DP](../1p3a-qbanks/amazon/raw/errors-in-zero-one-bang-subsequence-dp.txt) | Amazon | Single report | medium |
| [Word Segmentation with a Dictionary Helper](../1p3a-qbanks/amazon/raw/word-segmentation-dictionary-helper.txt) | Amazon | Single report | medium |
| [Permutations](../1p3a-qbanks/apple/raw/permutations.txt) | Apple | Single report | medium |
| [Subset Sum and All Subsets with Given Sum](../1p3a-qbanks/apple/raw/subset-sum-and-all-subsets.txt) | Apple | Single report | medium |
| [Longest Palindromic Substring](../1p3a-qbanks/bloomberg/raw/longest-palindromic-substring.txt) | Bloomberg | Single report | medium |
| [Round-Robin Tournament Schedule](../1p3a-qbanks/bloomberg/raw/round-robin-tournament-schedule.txt) | Bloomberg | Single report | hard |
| [Trapping Rain Water](../1p3a-qbanks/bloomberg/raw/trapping-rain-water.txt) | Bloomberg | Single report | hard |
| [Bounded Number Construction from Allowed Digits](../1p3a-qbanks/bytedance/raw/bounded-number-from-allowed-digits.txt) | ByteDance / TikTok | Single report | hard |
| [Maximum Non-Adjacent Chocolates](../1p3a-qbanks/cisco/raw/maximum-non-adjacent-chocolates.txt) | Cisco | Single report | easy |
| [Maximum Subarray](../1p3a-qbanks/cisco/raw/maximum-subarray.txt) | Cisco | Single report | medium |
| [Non-Consecutive Process Allocation Counting (Modulo 1e9 + 7)](../1p3a-qbanks/citadel/raw/grace-hopper-non-consecutive-processes.txt) | Citadel / Citadel Securities | Single report | easy |
| [Wildcard / Regex String Matching (`*` operator)](../1p3a-qbanks/citadel/raw/wildcard-regex-matching.txt) | Citadel / Citadel Securities | Single report | medium |
| [House Robber Series](../1p3a-qbanks/databricks/raw/house-robber-series.txt) | Databricks | Single report | medium |
| [String Pattern Partition](../1p3a-qbanks/databricks/raw/string-pattern-partition.txt) | Databricks | Single report | medium |
| [Code Craft: Common Restaurant Pickup Order (LCS Variant)](../1p3a-qbanks/doordash/raw/code-craft-common-restaurant-list.txt) | DoorDash | Single report | medium |
| [Count Perfect Wake Numbers up to N](../1p3a-qbanks/google/raw/perfect-wake-number-count.txt) | Google | Single report | hard |
| [Count Word Decompositions into Chemical Element Symbols](../1p3a-qbanks/google/raw/periodic-table-word-decomposition.txt) | Google | Single report | medium |
| [Fence Painting Minimum Operations (Gemini Team)](../1p3a-qbanks/google/raw/fence-painting-min-ops.txt) | Google | Single report | medium |
| [Jump Game with Profit](../1p3a-qbanks/google/raw/jump-game-with-profit.txt) | Google | Single report | medium |
| [Russian Doll Envelopes](../1p3a-qbanks/google/raw/russian-doll-envelopes.txt) | Google | Single report | medium |
| [Target Expressions from Ordered Digits](../1p3a-qbanks/google/raw/target-expressions-ordered-digits.txt) | Google | Single report | medium |
| [DNA Substring Palindrome Cost Sum](../1p3a-qbanks/intuit/raw/dna-substring-palindrome-cost-sum.txt) | Intuit | Single report | medium |
| [Domino Tiling Recurrences](../1p3a-qbanks/jpmorgan/raw/domino-tiling-recurrences.txt) | JPMorgan Chase | Single report | medium |
| [Best Time to Buy and Sell Stock](../1p3a-qbanks/meta/raw/best-time-to-buy-sell-stock.txt) | Meta | Single report | easy |
| [Delete and Earn (LC 740)](../1p3a-qbanks/microsoft/raw/delete-and-earn.txt) | Microsoft | Single report | medium |
| [Coin Change (LC 322)](../1p3a-qbanks/pinterest/raw/coin-change.txt) | Pinterest | Single report | medium |
| [Regenerate Strings from Run-Length Digit Encoding](../1p3a-qbanks/pinterest/raw/string-from-rle-digits.txt) | Pinterest | Single report | medium |
| [Campaign Cost Weekly Partition](../1p3a-qbanks/salesforce/raw/campaign-cost-weekly-partition.txt) | Salesforce | Single report | medium |
| [Valid Word Abbreviation (LeetCode 408) with Follow-Up Variant](../1p3a-qbanks/salesforce/raw/valid-word-abbreviation.txt) | Salesforce | Single report | medium |
| [Check if an Original String Exists Given Two Encoded Strings](../1p3a-qbanks/snowflake/raw/encoded-string-original-exists.txt) | Snowflake | Single report | hard |
| [Connect Four - Can Play Win](../1p3a-qbanks/snowflake/raw/connect-four-can-play-win.txt) | Snowflake | Single report | medium |
| [Min Coins to Pay with Change Allowed](../1p3a-qbanks/snowflake/raw/min-coins-with-change.txt) | Snowflake | Single report | medium |
| [N-Queens](../1p3a-qbanks/snowflake/raw/n-queens.txt) | Snowflake | Single report | hard |
| [Count Longest Increasing Subsequences](../1p3a-qbanks/pinduoduo/raw/count-longest-increasing-subsequences.txt) | Temu / Pinduoduo | Single report | medium |
| [Number of Ways to Earn Points](../1p3a-qbanks/uber/raw/number-of-ways-to-earn-points.txt) | Uber | Single report | medium |
| [Number of Ways to Wear Different Hats to Each Other](../1p3a-qbanks/uber/raw/number-of-ways-to-wear-hats.txt) | Uber | Single report | hard |
| [Word Break](../1p3a-qbanks/uber/raw/word-break.txt) | Uber | Single report | medium |
| [Build an Arithmetic Expression to Reach a Target](../1p3a-qbanks/waymo/raw/arithmetic-expression-reach-target.txt) | Waymo | Single report | medium |
| [House Robber II (LC 213)](../1p3a-qbanks/waymo/raw/house-robber-ii-lc-213.txt) | Waymo | Single report | medium |
| [Regular Expression Matching (LC 10)](../1p3a-qbanks/waymo/raw/regular-expression-matching-lc-10.txt) | Waymo | Single report | hard |

</details>

<details><summary><b>math-greedy-sort</b> (65)</summary>

| Question | Company | Freq | Level |
|---|---|---|---|
| [Beat the Odds — Probability Test](../1p3a-qbanks/optiver/raw/beat-the-odds-probability.txt) | Optiver | Very high | – |
| [Number Logic — Sequence / Find the Pattern](../1p3a-qbanks/optiver/raw/number-logic-sequence.txt) | Optiver | High | – |
| [Trading Betting Game (EV / Market-Making)](../1p3a-qbanks/optiver/raw/trading-betting-game.txt) | Optiver | High | – |
| [Task Manager Filter / Sort / Dedupe](../1p3a-qbanks/rippling/raw/task-manager-filter-sort-dedupe.txt) | Rippling | High | medium |
| [Minimum Swaps to Sort (Cycle Decomposition)](../1p3a-qbanks/akunacapital/raw/minimum-swaps-to-sort-cycle.txt) | Akuna Capital | Medium | medium |
| [Quant OA Math / Probability Multiple Choice Bank](../1p3a-qbanks/goldmansachs/raw/quant-oa-math-probability-bank.txt) | Goldman Sachs | Medium | medium |
| [Minimum Sum-of-Distances Meeting Point on a Line](../1p3a-qbanks/linkedin/raw/coding-min-sum-distance-line.txt) | LinkedIn | Medium | medium |
| [Resource Allocation / Balancing Teams Equal Sum](../1p3a-qbanks/mathworks/raw/resource-allocation-equal-sum.txt) | MathWorks | Medium | easy |
| [Days Between Two Dates](../1p3a-qbanks/optiver/raw/days-between-dates.txt) | Optiver | Medium | easy |
| [QR OA — Efficient Univariate OLS Regression](../1p3a-qbanks/twosigma/raw/qr-oa-efficient-univariate-ols.txt) | Two Sigma | Medium | medium |
| [QR Statistics / Domain Knowledge Oral](../1p3a-qbanks/twosigma/raw/qr-statistics-domain-oral.txt) | Two Sigma | Medium | hard |
| [Phone Screen: Minimize a Convex Function](../1p3a-qbanks/uber/raw/phone-screen-convex-function-min.txt) | Uber | Medium | medium |
| [Minimum Days to Release Updates](../1p3a-qbanks/akunacapital/raw/minimum-days-to-release-updates.txt) | Akuna Capital | Low | medium |
| [QR Video OA: Advanced Math and Reasoning](../1p3a-qbanks/akunacapital/raw/qr-video-oa-advanced-math-and-reasoning.txt) | Akuna Capital | Low | hard |
| [Trader Fair-Bet Probability and Number Sequences](../1p3a-qbanks/akunacapital/raw/trader-fair-bet-probability-and-number-sequences.txt) | Akuna Capital | Low | medium |
| [Package Weight Rearrangement](../1p3a-qbanks/amazon/raw/adjacent-package-weight-rearrangement.txt) | Amazon | Low | medium |
| [Minimum Cost Flight Split](../1p3a-qbanks/bloomberg/raw/minimum-cost-flight-split.txt) | Bloomberg | Low | medium |
| [GQS Quant Phone — Correlation Range + Box-Muller-Style Sampling](../1p3a-qbanks/citadel/raw/gqs-quant-phone-brainteasers.txt) | Citadel / Citadel Securities | Low | hard |
| [Efficient Tasks / Three-Server Difficulty Split](../1p3a-qbanks/goldmansachs/raw/efficient-tasks-three-server-difficulty.txt) | Goldman Sachs | Low | medium |
| [Quant Strat Superday Probability + Fixed Income Drill](../1p3a-qbanks/goldmansachs/raw/quant-strat-superday-probability-fixed-income.txt) | Goldman Sachs | Low | medium |
| [Minimum Replacements for Adjacent Duplicate Characters](../1p3a-qbanks/ibm/raw/minimum-replacements-adjacent-duplicates.txt) | IBM | Low | easy |
| [Quant Probability and Statistics Orals](../1p3a-qbanks/jpmorgan/raw/quant-probability-and-statistics-orals.txt) | JPMorgan Chase | Low | medium |
| [Biased Coin to Uniform Range](../1p3a-qbanks/linkedin/raw/coding-biased-coin-uniform.txt) | LinkedIn | Low | medium |
| [Pow(x, n)](../1p3a-qbanks/meta/raw/pow-x-n.txt) | Meta | Low | easy |
| [Balanced Permutation Prefix Check](../1p3a-qbanks/microsoft/raw/balanced-permutation-prefix.txt) | Microsoft | Low | medium |
| [Count Positive Pairs Satisfying 1/x + 1/y = 1/N](../1p3a-qbanks/microsoft/raw/equations-reciprocal-pair-count.txt) | Microsoft | Low | medium |
| [Likelihood / Event-Ordering Test](../1p3a-qbanks/optiver/raw/likelihood-ordering-test.txt) | Optiver | Low | – |
| [Minimum Operations to Reduce an Integer to 0 (LeetCode 2571)](../1p3a-qbanks/salesforce/raw/minimum-operations-reduce-to-zero.txt) | Salesforce | Low | medium |
| [OA: Minimum Operations to Reduce N to 0 (±2^i)](../1p3a-qbanks/uber/raw/oa-min-ops-to-zero-power-of-two.txt) | Uber | Low | medium |
| [Find Missing and Repeated Element](../1p3a-qbanks/akunacapital/raw/find-missing-and-repeated-element.txt) | Akuna Capital | Single report | medium |
| [Longest Consecutive Sequence — Adjacent Gap ≤ K](../1p3a-qbanks/amazon/raw/longest-consecutive-subsequence-bounded-gap.txt) | Amazon | Single report | medium |
| [Minimum Bills and Coins for an Amount](../1p3a-qbanks/amazon/raw/min-bills-coins-change.txt) | Amazon | Single report | easy |
| [Minimum Range-Add Ops to Non-Decreasing Array](../1p3a-qbanks/amazon/raw/min-ops-non-decreasing-via-range-add.txt) | Amazon | Single report | medium |
| [Minimum-Point Redemption Across Programs](../1p3a-qbanks/amazon/raw/minimum-points-redemption.txt) | Amazon | Single report | medium |
| [Best Time to Buy and Sell Stock](../1p3a-qbanks/apple/raw/best-time-to-buy-and-sell-stock.txt) | Apple | Single report | easy |
| [Best Time to Buy and Sell Stock II](../1p3a-qbanks/apple/raw/best-time-to-buy-and-sell-stock-ii.txt) | Apple | Single report | easy |
| [Beautify Houses to Strict Monotone](../1p3a-qbanks/capitalone/raw/beautify-houses-monotone.txt) | Capital One | Single report | medium |
| [DS Business Analyst Case (P&L Decomposition)](../1p3a-qbanks/capitalone/raw/ds-business-analyst-case-study.txt) | Capital One | Single report | medium |
| [Model Risk: VaR & Monte Carlo Orals](../1p3a-qbanks/capitalone/raw/model-risk-quant-orals.txt) | Capital One | Single report | – |
| [Odd Zero-Digit Count](../1p3a-qbanks/capitalone/raw/odd-zero-digit-count.txt) | Capital One | Single report | easy |
| [Candy (LC 135)](../1p3a-qbanks/citadel/raw/candy-lc-135.txt) | Citadel / Citadel Securities | Single report | – |
| [Trading OA — 15-Question Probability Quickfire (30 Minutes)](../1p3a-qbanks/citadel/raw/trading-probability-quickfire.txt) | Citadel / Citadel Securities | Single report | medium |
| [When Does `min x'Qx + c'x` Have a Finite Minimum?](../1p3a-qbanks/citadel/raw/quadratic-form-finite-min.txt) | Citadel / Citadel Securities | Single report | hard |
| [25 Horses, 5 Lanes Puzzle](../1p3a-qbanks/goldmansachs/raw/horse-race-25.txt) | Goldman Sachs | Single report | medium |
| [Valid Triangle + Point Inclusion](../1p3a-qbanks/goldmansachs/raw/valid-triangle-point-inclusion.txt) | Goldman Sachs | Single report | medium |
| [Find All Bad Pairs Using runTest Oracle](../1p3a-qbanks/google/raw/find-bad-pairs-with-oracle.txt) | Google | Single report | hard |
| [Wordle-Style 5-Letter Minimum-Guess Strategy](../1p3a-qbanks/google/raw/wordle-min-guess-strategy.txt) | Google | Single report | hard |
| [Minimum Swaps to Sort Descending](../1p3a-qbanks/jpmorgan/raw/minimum-swaps-sort-descending.txt) | JPMorgan Chase | Single report | medium |
| [Largest Number (LC 179)](../1p3a-qbanks/linkedin/raw/coding-largest-number.txt) | LinkedIn | Single report | – |
| [Pow(x, n) (LC 50)](../1p3a-qbanks/linkedin/raw/coding-pow-x-n.txt) | LinkedIn | Single report | medium |
| [Commuter Coupon Conditional Probability](../1p3a-qbanks/lyft/raw/ds-commuter-coupon-probability.txt) | Lyft | Single report | medium |
| [Two-Intern Task Assignment Max Reward](../1p3a-qbanks/mathworks/raw/two-intern-task-assignment-max-reward.txt) | MathWorks | Single report | medium |
| [Reaching Points](../1p3a-qbanks/nvidia/raw/reaching-points.txt) | NVIDIA | Single report | hard |
| [QR Technical Round — Brainteasers & Estimation](../1p3a-qbanks/optiver/raw/qr-technical-brainteasers.txt) | Optiver | Single report | – |
| [Monotone Increasing Digits (LC 738)](../1p3a-qbanks/oracle/raw/monotone-increasing-digits.txt) | Oracle | Single report | medium |
| [Two-Value Exchange Warm-Up](../1p3a-qbanks/oracle/raw/swap-two-values-without-temp.txt) | Oracle | Single report | easy |
| [Stream Sampling & Distribution Verification](../1p3a-qbanks/perplexity/raw/stream-sampling-and-distribution-verification.txt) | Perplexity | Single report | medium |
| [Weighted Sampling from Score Distribution](../1p3a-qbanks/pinterest/raw/weighted-sampling-from-scores.txt) | Pinterest | Single report | medium |
| [Cursor-Based Pagination Over Sorted Logs](../1p3a-qbanks/roblox/raw/cursor-based-pagination-over-sorted-logs.txt) | Roblox | Single report | medium |
| [Find the Closest Palindrome (LeetCode 564)](../1p3a-qbanks/roblox/raw/find-the-closest-palindrome.txt) | Roblox | Single report | hard |
| [Single Query Type, Max Revenue in K Minutes](../1p3a-qbanks/snowflake/raw/single-query-max-revenue.txt) | Snowflake | Single report | easy |
| [Count Same-Bit Numbers](../1p3a-qbanks/sofi/raw/count-same-bit-numbers.txt) | SoFi | Single report | medium |
| [Battery Cell Quality Statistical Case](../1p3a-qbanks/tesla/raw/battery-cell-quality-statistical-case.txt) | Tesla | Single report | medium |
| [Dojo Pythonic Coding Pair: Permutation Check and Pow](../1p3a-qbanks/tesla/raw/dojo-permutation-and-pow.txt) | Tesla | Single report | medium |
| [K Shuttle Pickup Locations](../1p3a-qbanks/uber/raw/k-shuttle-pickup-locations.txt) | Uber | Single report | medium |

</details>

<details><summary><b>coding-unclassified</b> (65)</summary>

| Question | Company | Freq | Level |
|---|---|---|---|
| [AI Assistant Repo Bug-Fix (OA Part 2)](../1p3a-qbanks/amazon/raw/ai-assistant-repo-bug-fix.txt) | Amazon | Very high | medium |
| [Agents / Coding with LLMs](../1p3a-qbanks/anthropic/raw/agents-coding-llm-tool-use.txt) | Anthropic | High | medium |
| [AI-Assisted Coding Round (HackerRank)](../1p3a-qbanks/stripe/raw/ai-assisted-coding-round.txt) | Stripe | High | medium |
| [Mako Debug (Bug Bash)](../1p3a-qbanks/stripe/raw/mako-debug.txt) | Stripe | High | hard |
| [Coding Q4 — Distributed Mode / Median](../1p3a-qbanks/anthropic/raw/coding-q4-distributed-mode-median.txt) | Anthropic | Medium | hard |
| [Full-Stack AI-Pairing Assessment](../1p3a-qbanks/instacart/raw/full-stack-ai-assessment.txt) | Instacart | Medium | hard |
| [Streaming Entropy (numerical stability + online accumulation)](../1p3a-qbanks/openai/raw/streaming-entropy.txt) | OpenAI | Medium | medium |
| [Frontend Challenge — DOM Flag Extraction + React Wordle](../1p3a-qbanks/ramp/raw/frontend-challenge-dom-wordle.txt) | Ramp | Medium | medium |
| [LLM API Practical and Output Validation](../1p3a-qbanks/scale.ai/raw/llm-api-practical-and-output-validation.txt) | Scale AI | Medium | – |
| [OA: Maximize Pipeline Throughput](../1p3a-qbanks/uber/raw/oa-pipeline-throughput.txt) | Uber | Medium | medium |
| [Performance Engineer Take-Home — Kernel Optimization](../1p3a-qbanks/anthropic/raw/performance-engineer-take-home.txt) | Anthropic | Low | hard |
| [Prompting and Engineering with LLMs (Colab)](../1p3a-qbanks/anthropic/raw/prompting-engineering-with-llms.txt) | Anthropic | Low | medium |
| [AI Enhanced Pilot — Domain (AI-Assisted Coding Round)](../1p3a-qbanks/coinbase/raw/ai-enhanced-pilot-domain.txt) | Coinbase | Low | – |
| [Frontend Domain — Card / Validation Component to Mockup](../1p3a-qbanks/coinbase/raw/fe-domain-card-component.txt) | Coinbase | Low | medium |
| [Frontend OA — Render Blog Posts (Multi-Stage CodeSignal)](../1p3a-qbanks/coinbase/raw/fe-render-blog-posts-oa.txt) | Coinbase | Low | medium |
| [Frontend VO: Build UI from Mockup with API](../1p3a-qbanks/doordash/raw/frontend-vo-build-ui.txt) | DoorDash | Low | medium |
| [RAG Notebook (ML Coding)](../1p3a-qbanks/harvey/raw/rag-ml-coding-notebook.txt) | Harvey | Low | medium |
| [Frontend CSS Flexbox Layout Task](../1p3a-qbanks/ibm/raw/frontend-css-flexbox-layout.txt) | IBM | Low | easy |
| [DE AI-Native Full-Stack Round](../1p3a-qbanks/meta/raw/de-ai-native-full-stack-round.txt) | Meta | Low | hard |
| [Weekly Calendar (React)](../1p3a-qbanks/robinhood/raw/weekly-calendar-react.txt) | Robinhood | Low | medium |
| [Frontend Curry: addTwoNumbers → addThreeNumbers(a)(b)(c)](../1p3a-qbanks/salesforce/raw/frontend-currying-add-numbers.txt) | Salesforce | Low | easy |
| [Existing Codebase Debugging Round](../1p3a-qbanks/scale.ai/raw/existing-codebase-debugging-round.txt) | Scale AI | Low | medium |
| [HTML/CSS Search Bar and Lazy-Loading List](../1p3a-qbanks/sofi/raw/html-css-search-bar-and-lazy-loading-list.txt) | SoFi | Low | medium |
| [Debug: Moshi JSON Library](../1p3a-qbanks/stripe/raw/moshi-debug.txt) | Stripe | Low | medium |
| [Requests Library Debug (BugSquash)](../1p3a-qbanks/stripe/raw/requests-library-debug.txt) | Stripe | Low | hard |
| [SnakeYAML Debug (Java)](../1p3a-qbanks/stripe/raw/snakeyaml-debug.txt) | Stripe | Low | hard |
| [VO: AI-Assisted Coding / Debug Round (HackerRank IDE + AI)](../1p3a-qbanks/uber/raw/vo-ai-assisted-debug-round.txt) | Uber | Low | medium |
| [Frontend Practical Coding — Work Against a Provided API](../1p3a-qbanks/vanta/raw/frontend-api-practical.txt) | Vanta | Low | medium |
| [SQL: Cloud Resource Utilization Dashboard](../1p3a-qbanks/akunacapital/raw/sql-cloud-resource-utilization-dashboard.txt) | Akuna Capital | Single report | easy |
| [Numbered Buttons with Data Popovers](../1p3a-qbanks/amazon/raw/frontend-numbered-popovers.txt) | Amazon | Single report | medium |
| [Dynamic Suffix Word Search](../1p3a-qbanks/anduril/raw/dynamic-suffix-word-search.txt) | Anduril Industries | – | – |
| [Evaluate a Flat Addition/Subtraction Expression](../1p3a-qbanks/anduril/raw/evaluate-a-flat-addition-subtraction-expression.txt) | Anduril Industries | – | – |
| [Find Unreachable Heap Objects from Stack Addresses](../1p3a-qbanks/anduril/raw/find-unreachable-heap-objects-from-stack-addresses.txt) | Anduril Industries | – | – |
| [Find Unreachable Heap Objects with References](../1p3a-qbanks/anduril/raw/find-unreachable-heap-objects-with-references.txt) | Anduril Industries | – | – |
| [Max Profit from Cutting Rods into Equal Sale Length](../1p3a-qbanks/anduril/raw/max-profit-from-cutting-rods-into-equal-sale-length.txt) | Anduril Industries | – | – |
| [Valid Palindrome](../1p3a-qbanks/apple/raw/valid-palindrome.txt) | Apple | Single report | easy |
| [NYC Green Taxi Data Science Challenge](../1p3a-qbanks/capitalone/raw/nyc-green-taxi-data-science-challenge.txt) | Capital One | Single report | – |
| [SRE Python Fundamentals — Five-Question Set](../1p3a-qbanks/citadel/raw/sre-python-fundamentals-set.txt) | Citadel / Citadel Securities | Single report | easy |
| [Kubernetes Controller for Pod Balance](../1p3a-qbanks/ibm/raw/kubernetes-controller-pod-balance.txt) | IBM | Single report | medium |
| [Pandas Meeting Work-Duration Calculation](../1p3a-qbanks/intuit/raw/pandas-meeting-work-duration.txt) | Intuit | Single report | medium |
| [Nested List Weight Sum II (LC 364)](../1p3a-qbanks/linkedin/raw/coding-nested-list-weight-sum-ii.txt) | LinkedIn | Single report | – |
| [AI Project Round (Take-Home-Style)](../1p3a-qbanks/meta/raw/ai-project-take-home-style-round.txt) | Meta | Single report | hard |
| [Count Binary Substrings (LC 696)](../1p3a-qbanks/microsoft/raw/count-binary-substrings.txt) | Microsoft | Single report | easy |
| [Build an Auth0-based MCP Server (Hands-on OA)](../1p3a-qbanks/okta/raw/oa-mcp-auth0-server.txt) | Okta | Single report | medium |
| [Python + SQL Technical Round (Typing, Debugging, Joins)](../1p3a-qbanks/okta/raw/python-sql-debugging-round.txt) | Okta | Single report | medium |
| [Code Reading — CLI Tool Feature Audit](../1p3a-qbanks/oracle/raw/code-reading-cli-tool-refactor.txt) | Oracle | Single report | medium |
| [REST API: Medical Records by Age](../1p3a-qbanks/oracle/raw/rest-api-medical-records-by-age.txt) | Oracle | Single report | medium |
| [Frontend Discover Infinite Scroll](../1p3a-qbanks/perplexity/raw/frontend-discover-infinite-scroll.txt) | Perplexity | Single report | medium |
| [AI-Assisted Receipt Upload and Transaction Matching](../1p3a-qbanks/ramp/raw/ai-receipt-upload-matching.txt) | Ramp | Single report | medium |
| [Load Balancer Debugging & Extension](../1p3a-qbanks/reddit/raw/load-balancer-debugging-and-extension.txt) | Reddit | Single report | medium |
| [Analytics Engineer: SQL + Python Sessionization](../1p3a-qbanks/robinhood/raw/analytics-engineer-sql-python.txt) | Robinhood | Single report | – |
| [Piano Keys: Vanilla UI Interaction](../1p3a-qbanks/roblox/raw/piano-keys-vanilla-ui.txt) | Roblox | Single report | medium |
| [Code Review OA — Python Files (Salesforce / Slack)](../1p3a-qbanks/salesforce/raw/code-review-oa.txt) | Salesforce | Single report | medium |
| [SQL Pair Programming (Data Engineer)](../1p3a-qbanks/shopify/raw/sql-pair-programming-de.txt) | Shopify | Single report | medium |
| [React / TypeScript Kanban Board](../1p3a-qbanks/snowflake/raw/react-typescript-kanban-board.txt) | Snowflake | Single report | medium |
| [SQL: Project Duration & Budget per Employee](../1p3a-qbanks/snowflake/raw/sql-project-duration-budget-per-employee.txt) | Snowflake | Single report | medium |
| [Karat AI Coding: Accessibility Feature](../1p3a-qbanks/sofi/raw/karat-ai-coding-accessibility-feature.txt) | SoFi | Single report | medium |
| [React Loan Estimator](../1p3a-qbanks/sofi/raw/react-loan-estimator.txt) | SoFi | Single report | medium |
| [Star Rating Component](../1p3a-qbanks/sofi/raw/star-rating-component.txt) | SoFi | Single report | medium |
| [Colander Validation Debug (BugSquash)](../1p3a-qbanks/stripe/raw/colander-validation-debug.txt) | Stripe | Single report | hard |
| [Review Assignment (JGit)](../1p3a-qbanks/stripe/raw/review-assignment-jgit.txt) | Stripe | Single report | medium |
| [User Login Streaks (SQL)](../1p3a-qbanks/pinduoduo/raw/sql-login-streak.txt) | Temu / Pinduoduo | Single report | medium |
| [Data Cleaning Pipeline and SQL Analytics Screen](../1p3a-qbanks/tesla/raw/data-engineering-cleaning-and-sql-screen.txt) | Tesla | Single report | medium |
| [Toy Order Completion Analysis](../1p3a-qbanks/uber/raw/toy-order-completion-analysis.txt) | Uber | Single report | easy |
| [Android Phone Screen: Build a Pickup Screen in Jetpack Compose](../1p3a-qbanks/waymo/raw/android-pickup-screen-jetpack-compose.txt) | Waymo | Single report | medium |

</details>

<details><summary><b>hashing-counting</b> (64)</summary>

| Question | Company | Freq | Level |
|---|---|---|---|
| [Loan Aggregation & Transaction Matching (Parent-Company Resolution)](../1p3a-qbanks/affirm/raw/loan-aggregation-transaction-matching.txt) | Affirm | High | medium |
| [Fraud Detection: Debug + Event Matching](../1p3a-qbanks/affirm/raw/fraud-detection-event-matching.txt) | Affirm | Medium | medium |
| [Split Stay — Two-Listing Date Coverage](../1p3a-qbanks/airbnb/raw/split-stay-availability-combinations.txt) | Airbnb | Medium | medium |
| [Highest Average Score Per Person](../1p3a-qbanks/goldmansachs/raw/max-average-score-per-person.txt) | Goldman Sachs | Medium | easy |
| [OA: Largest Group Sharing a Digit](../1p3a-qbanks/google/raw/oa-group-two-digit-numbers-shared-digit.txt) | Google | Medium | easy |
| [Transaction Aggregation by 24-Hour Buckets](../1p3a-qbanks/affirm/raw/transaction-24h-aggregation.txt) | Affirm | Low | medium |
| [Maximum Distinct Elements After K Swaps](../1p3a-qbanks/akunacapital/raw/maximum-distinct-after-k-swaps.txt) | Akuna Capital | Low | medium |
| [Log Aggregation and Group-By](../1p3a-qbanks/amazon/raw/log-aggregation-and-grouping.txt) | Amazon | Low | medium |
| [Design a HashMap](../1p3a-qbanks/apple/raw/design-a-hashmap.txt) | Apple | Low | medium |
| [Code Craft: Similar Restaurant Names (K-Swap Anagram)](../1p3a-qbanks/doordash/raw/code-craft-similar-restaurant-names.txt) | DoorDash | Low | easy |
| [First Unique Character](../1p3a-qbanks/goldmansachs/raw/first-unique-character.txt) | Goldman Sachs | Low | easy |
| [Max Distinct Counts After Splitting Array](../1p3a-qbanks/ibm/raw/distinct-count-split.txt) | IBM | Low | easy |
| [Longest Consecutive Sequence](../1p3a-qbanks/meta/raw/longest-consecutive-sequence.txt) | Meta | Low | medium |
| [Count Disjoint String Pairs](../1p3a-qbanks/netflix/raw/count-disjoint-string-pairs.txt) | Netflix | Low | medium |
| [Secret Santa / Gift Assignment from CSV](../1p3a-qbanks/shopify/raw/secret-santa-gift-assignment.txt) | Shopify | Low | easy |
| [GIF Info Pagination by Tag](../1p3a-qbanks/sofi/raw/gif-info-pagination-by-tag.txt) | SoFi | Low | medium |
| [Dataset Join (joinDataSet OA)](../1p3a-qbanks/stripe/raw/dataset-join-oa.txt) | Stripe | Low | medium |
| [OA: Permutation Prefix Balanced (1..k Subarray Check)](../1p3a-qbanks/uber/raw/oa-permutation-prefix-balanced.txt) | Uber | Low | medium |
| [Fun With Anagrams (Deduplicate Anagrams)](../1p3a-qbanks/akunacapital/raw/fun-with-anagrams-dedup.txt) | Akuna Capital | Single report | easy |
| [Abusive Books in Reading Event Stream](../1p3a-qbanks/amazon/raw/audible-abusive-books-stream.txt) | Amazon | Single report | medium |
| [Clone Linked List with Random + Next Pointer](../1p3a-qbanks/amazon/raw/clone-linked-list-random-and-next-pointer.txt) | Amazon | Single report | medium |
| [Count Repeat Customers Across Dates](../1p3a-qbanks/amazon/raw/repeat-customers-across-dates.txt) | Amazon | Single report | medium |
| [Equal-Frequency Block Prefix Count](../1p3a-qbanks/amazon/raw/equal-block-prefix-via-prefix-hash.txt) | Amazon | Single report | hard |
| [Frequency-Priority Error Code Sort](../1p3a-qbanks/amazon/raw/frequency-priority-error-code-sort.txt) | Amazon | Single report | easy |
| [Group Anagrams (LC 49)](../1p3a-qbanks/amazon/raw/group-anagrams-lc-49.txt) | Amazon | Single report | medium |
| [In-Flight Movie Pair (Two Sum Variant)](../1p3a-qbanks/amazon/raw/in-flight-movie-pair-two-sum.txt) | Amazon | Single report | easy |
| [Dedupe Unsorted Array (Three Variants)](../1p3a-qbanks/apple/raw/dedupe-unsorted-array-three-variants.txt) | Apple | Single report | medium |
| [Group Anagrams](../1p3a-qbanks/apple/raw/group-anagrams.txt) | Apple | Single report | medium |
| [Happy Number Service](../1p3a-qbanks/apple/raw/happy-number.txt) | Apple | Single report | easy |
| [Numbers with No Neighbors](../1p3a-qbanks/apple/raw/numbers-with-no-neighbors.txt) | Apple | Single report | medium |
| [Zero Out Duplicates and Sort Without Native Sort](../1p3a-qbanks/apple/raw/zero-out-duplicates-and-sort-without-native-sort.txt) | Apple | Single report | easy |
| [Shopping Category Trip Difference](../1p3a-qbanks/atlassian/raw/shopping-category-trip-difference.txt) | Atlassian | Single report | medium |
| [Equal Character Frequency (Remove-One Variant)](../1p3a-qbanks/bloomberg/raw/equal-character-frequency.txt) | Bloomberg | Single report | medium |
| [Pythagorean Triple in an Integer Array](../1p3a-qbanks/bytedance/raw/pythagorean-triple-integer-array.txt) | ByteDance / TikTok | Single report | medium |
| [Maximum Drop Points in One Line](../1p3a-qbanks/cisco/raw/maximum-drop-points-in-one-line.txt) | Cisco | Single report | easy |
| [Odd Birthday Date Count](../1p3a-qbanks/cisco/raw/odd-birthday-date-count.txt) | Cisco | Single report | easy |
| [Anagram Queries on Word List](../1p3a-qbanks/goldmansachs/raw/anagram-queries.txt) | Goldman Sachs | Single report | easy |
| [Count Unique Pairs With Difference K](../1p3a-qbanks/goldmansachs/raw/unique-pairs-difference-k.txt) | Goldman Sachs | Single report | easy |
| [Equalize Letter Frequencies With Add / Remove](../1p3a-qbanks/goldmansachs/raw/equalize-letter-frequencies.txt) | Goldman Sachs | Single report | medium |
| [Hash Collision: Causes, Resolution, Complexity](../1p3a-qbanks/goldmansachs/raw/hash-collision-fundamentals.txt) | Goldman Sachs | Single report | easy |
| [Second-Smallest Unique Element](../1p3a-qbanks/goldmansachs/raw/second-smallest-unique-element.txt) | Goldman Sachs | Single report | easy |
| [Gold Chain Split into Two Equal Halves](../1p3a-qbanks/google/raw/gold-chain-split-equal.txt) | Google | Single report | medium |
| [Progressive Array Partitioning](../1p3a-qbanks/google/raw/array-partition-unique-frequencies-straights.txt) | Google | Single report | medium |
| [Website Activity Analytics: Unique Users + Session Time](../1p3a-qbanks/google/raw/website-activity-analytics-unique-users-session-time.txt) | Google | Single report | medium |
| [Maximum Palindromes After Cross-String Swaps](../1p3a-qbanks/ibm/raw/maximum-palindromes-after-cross-string-swaps.txt) | IBM | Single report | medium |
| [Stratified Sampling from Class-Bucketed JSON](../1p3a-qbanks/linkedin/raw/coding-stratified-sample.txt) | LinkedIn | Single report | easy |
| [Group Anagrams](../1p3a-qbanks/meta/raw/lc-group-anagrams.txt) | Meta | Single report | easy |
| [Randomized Container](../1p3a-qbanks/meta/raw/randomized-container.txt) | Meta | Single report | medium |
| [Validate Lat/Long With Minimum Expensive API Calls](../1p3a-qbanks/microsoft/raw/geocode-api-call-minimization.txt) | Microsoft | Single report | medium |
| [Movie History Friends I / II](../1p3a-qbanks/netflix/raw/movie-history-friends.txt) | Netflix | Single report | medium |
| [Number Pairs That Match Target](../1p3a-qbanks/netflix/raw/number-pairs-matching-target.txt) | Netflix | Single report | medium |
| [Array Duplication — O(n) and Memory Trade-offs](../1p3a-qbanks/okta/raw/array-deduplication.txt) | Okta | Single report | medium |
| [Valid 5-Card Poker Straight](../1p3a-qbanks/oracle/raw/valid-poker-straight.txt) | Oracle | Single report | easy |
| [Maximum Number of Balls in a Box (LeetCode 1742)](../1p3a-qbanks/roblox/raw/maximum-number-of-balls-in-a-box.txt) | Roblox | Single report | easy |
| [Array Left / Right Duplicate Check (Binary Strings)](../1p3a-qbanks/salesforce/raw/array-left-right-duplicate-check.txt) | Salesforce | Single report | easy |
| [Maximum Number of Palindromic Strings (OA)](../1p3a-qbanks/salesforce/raw/maximum-palindromic-strings.txt) | Salesforce | Single report | medium |
| [Valid Set Cards](../1p3a-qbanks/snapchat/raw/valid-set-cards.txt) | Snapchat | Single report | medium |
| [Anagram Sentence Substitutions](../1p3a-qbanks/sofi/raw/anagram-sentence-substitutions.txt) | SoFi | Single report | medium |
| [Bulls and Cows with Per-Position Match Signal](../1p3a-qbanks/tesla/raw/bulls-and-cows-position-signal.txt) | Tesla | Single report | medium |
| [Isomorphic Strings](../1p3a-qbanks/uber/raw/isomorphic-strings.txt) | Uber | Single report | easy |
| [Two Sum with Smallest-Indices Tiebreak (+ 3 Sum Follow-up)](../1p3a-qbanks/walmartlabs/raw/two-sum-smallest-indices.txt) | Walmart Labs | Single report | medium |
| [Maximum-Area Axis-Aligned Rectangle from a Coordinate Set](../1p3a-qbanks/waymo/raw/max-area-rectangle-coordinate-set.txt) | Waymo | Single report | hard |
| [Sort Target String by Custom Order](../1p3a-qbanks/waymo/raw/custom-sort-string-by-order.txt) | Waymo | Single report | easy |
| [Maximum Distinct Integers After ±k Offset](../1p3a-qbanks/xai/raw/distinct-integers-after-offset.txt) | xAI | Single report | medium |

</details>

<details><summary><b>tree-trie</b> (61)</summary>

| Question | Company | Freq | Level |
|---|---|---|---|
| [Org Tree Lowest Common Department](../1p3a-qbanks/atlassian/raw/org-tree-lowest-common-department.txt) | Atlassian | High | medium |
| [Distributed Machine Cluster Count and Topology](../1p3a-qbanks/openai/raw/distributed-cluster-count-topology.txt) | OpenAI | High | medium |
| [Review — Token Tagging / Replacement](../1p3a-qbanks/airbnb/raw/review-token-replace.txt) | Airbnb | Medium | medium |
| [Coding Q6 — Longest-Match Tokenizer](../1p3a-qbanks/anthropic/raw/coding-q6-tokenizer.txt) | Anthropic | Medium | medium |
| [Fibonacci Tree Path](../1p3a-qbanks/databricks/raw/fibonacci-tree-path.txt) | Databricks | Medium | medium |
| [File Encryption Tree Optimization](../1p3a-qbanks/databricks/raw/file-encryption-tree-optimization.txt) | Databricks | Medium | medium |
| [Phone Keypad — Letter Combinations / Word Filter](../1p3a-qbanks/linkedin/raw/coding-phone-letter-combinations.txt) | LinkedIn | Medium | medium |
| [Lowest Common Ancestor (BT / BST / N-ary)](../1p3a-qbanks/meta/raw/lowest-common-ancestor.txt) | Meta | Medium | medium |
| [Build Binary Tree from Edges / S-Expression](../1p3a-qbanks/optiver/raw/binary-tree-from-edges.txt) | Optiver | Medium | medium |
| [In-Memory File System](../1p3a-qbanks/perplexity/raw/in-memory-file-system.txt) | Perplexity | Medium | medium |
| [Stop Sequences Before First Match](../1p3a-qbanks/perplexity/raw/stop-sequences-before-first-match.txt) | Perplexity | Medium | medium |
| [Trie Autocomplete / Prefix Search](../1p3a-qbanks/pinterest/raw/trie-autocomplete-prefix-search.txt) | Pinterest | Medium | medium |
| [Report Chain / Org Tree](../1p3a-qbanks/reddit/raw/report-chain-org-tree.txt) | Reddit | Medium | medium |
| [Middleware Router Wildcards and Path Params](../1p3a-qbanks/atlassian/raw/middleware-router-wildcards-path-params.txt) | Atlassian | Low | medium |
| [Greedy Longest-Match Dictionary Tokenizer](../1p3a-qbanks/google/raw/tokenize-longest-dict-match-greedy.txt) | Google | Low | medium |
| [AI Coding — Shared Substring in String List](../1p3a-qbanks/meta/raw/ai-coding-substring-from-list.txt) | Meta | Low | medium |
| [Org Chart Report Count (Tree Aggregation)](../1p3a-qbanks/microsoft/raw/org-chart-report-count.txt) | Microsoft | Low | medium |
| [Streaming Stop-Token Detection](../1p3a-qbanks/microsoft/raw/streaming-stop-token-detection.txt) | Microsoft | Low | hard |
| [Same N-ary Tree Comparison](../1p3a-qbanks/oracle/raw/same-nary-tree-comparison.txt) | Oracle | Low | easy |
| [Grant / Revoke / Check Access on Hierarchy](../1p3a-qbanks/pinterest/raw/grant-revoke-access-tree.txt) | Pinterest | Low | medium |
| [In-Memory File System / Directory Tree](../1p3a-qbanks/shopify/raw/in-memory-file-system.txt) | Shopify | Low | medium |
| [Hierarchical CSV Task / Subtask Formatter](../1p3a-qbanks/stripe/raw/hierarchical-csv-task-formatter.txt) | Stripe | Low | – |
| [Kth Smallest Element in a BST](../1p3a-qbanks/uber/raw/kth-smallest-element-bst.txt) | Uber | Low | medium |
| [Shortest Unique Substring](../1p3a-qbanks/affirm/raw/shortest-unique-substring.txt) | Affirm | Single report | medium |
| [Concatenated Words (LC 472) — Extended Variant](../1p3a-qbanks/amazon/raw/concatenated-words-output-combos.txt) | Amazon | Single report | hard |
| [Create Binary Tree from Descriptions (LC 2196)](../1p3a-qbanks/amazon/raw/create-binary-tree-from-descriptions-lc-2196.txt) | Amazon | Single report | medium |
| [Kth Smallest Element in a BST (LC 230)](../1p3a-qbanks/amazon/raw/kth-smallest-element-in-a-bst.txt) | Amazon | Single report | – |
| [Org-Chart Salary Aggregation](../1p3a-qbanks/amazon/raw/org-chart-salary-aggregation.txt) | Amazon | Single report | medium |
| [BST to Sorted Circular Doubly Linked List (LC 426)](../1p3a-qbanks/apple/raw/bst-to-sorted-circular-doubly-linked-list.txt) | Apple | Single report | medium |
| [Count Value Occurrences in a Binary Tree](../1p3a-qbanks/apple/raw/count-value-occurrences-in-a-binary-tree.txt) | Apple | Single report | medium |
| [Implement Trie (Prefix Tree)](../1p3a-qbanks/apple/raw/implement-trie-prefix-tree.txt) | Apple | Single report | medium |
| [Lazy Binary Tree Inorder Iterator](../1p3a-qbanks/apple/raw/lazy-binary-tree-inorder-iterator.txt) | Apple | Single report | medium |
| [Active Users Sliding Window](../1p3a-qbanks/bloomberg/raw/active-users-sliding-window.txt) | Bloomberg | Single report | medium |
| [Build a Nested Comment Tree from Flat Records](../1p3a-qbanks/bytedance/raw/build-nested-comment-tree.txt) | ByteDance / TikTok | Single report | medium |
| [Create Binary Tree from Descriptions (LeetCode 2196)](../1p3a-qbanks/bytedance/raw/create-binary-tree-from-descriptions.txt) | ByteDance / TikTok | Single report | – |
| [BST from Scratch (insert / search / delete) + Balancing Discussion](../1p3a-qbanks/citadel/raw/bst-from-scratch.txt) | Citadel / Citadel Securities | Single report | medium |
| [SQL Query Plan Optimizer](../1p3a-qbanks/databricks/raw/sql-query-plan-optimizer.txt) | Databricks | Single report | hard |
| [Code Craft: Hierarchical Path Key-Value Store](../1p3a-qbanks/doordash/raw/code-craft-path-key-value-store.txt) | DoorDash | Single report | medium |
| [Menu Tree Diff — Count Changed Nodes](../1p3a-qbanks/doordash/raw/code-craft-menu-tree-diff.txt) | DoorDash | Single report | medium |
| [ClayWorkspace Resource Tree](../1p3a-qbanks/goldmansachs/raw/clay-workspace-resource-tree.txt) | Goldman Sachs | Single report | medium |
| [Min × Max Product After Push / Pop](../1p3a-qbanks/goldmansachs/raw/min-max-product-stream.txt) | Goldman Sachs | Single report | medium |
| [Employee Hierarchy Add / Delete](../1p3a-qbanks/google/raw/employee-hierarchy-add-delete.txt) | Google | Single report | medium |
| [Minimum-Cost Tree Disconnection](../1p3a-qbanks/google/raw/binary-tree-root-leaf-minimum-cut.txt) | Google | Single report | – |
| [Nested List Weight Sum (LC 339) with Follow-Ups](../1p3a-qbanks/google/raw/nested-list-weighted-sum-parsing.txt) | Google | Single report | medium |
| [Streaming Insert + Find K-th Largest](../1p3a-qbanks/google/raw/insert-find-kth-largest.txt) | Google | Single report | medium |
| [Shortest Unique Prefix](../1p3a-qbanks/meta/raw/shortest-unique-prefix.txt) | Meta | Single report | medium |
| [Convert BST to Sorted Doubly Linked List (LC 426)](../1p3a-qbanks/pinterest/raw/bst-to-doubly-linked-list.txt) | Pinterest | Single report | medium |
| [Closest Binary Search Tree Value Variant (LeetCode 270)](../1p3a-qbanks/roblox/raw/closest-binary-search-tree-value-variant.txt) | Roblox | Single report | easy |
| [Implement Trie (LeetCode 208)](../1p3a-qbanks/roblox/raw/implement-trie.txt) | Roblox | Single report | medium |
| [Remove Prefix Strings](../1p3a-qbanks/roblox/raw/remove-prefix-strings.txt) | Roblox | Single report | medium |
| [Kth Smallest in a BST (LeetCode 230) with Follow-Ups](../1p3a-qbanks/salesforce/raw/kth-smallest-bst-follow-ups.txt) | Salesforce | Single report | medium |
| [Lowest Common Ancestor with Parent Pointers (LeetCode 1650)](../1p3a-qbanks/salesforce/raw/lowest-common-ancestor-parent-pointers.txt) | Salesforce | Single report | medium |
| [Forest Parent Array Delete Node](../1p3a-qbanks/snowflake/raw/forest-parent-array-delete-node.txt) | Snowflake | Single report | medium |
| [In-Memory File System](../1p3a-qbanks/snowflake/raw/in-memory-file-system.txt) | Snowflake | Single report | medium |
| [Serialize and Deserialize Dictionary Trie](../1p3a-qbanks/snowflake/raw/serialize-and-deserialize-dictionary-trie.txt) | Snowflake | Single report | medium |
| [Throne Inheritance](../1p3a-qbanks/snowflake/raw/throne-inheritance.txt) | Snowflake | Single report | medium |
| [RBAC Role Resolver](../1p3a-qbanks/stripe/raw/rbac-role-resolver.txt) | Stripe | Single report | medium |
| [Spatial Quadtree: Insert and Range Query](../1p3a-qbanks/uber/raw/spatial-quadtree-range-query.txt) | Uber | Single report | medium |
| [Serialize Arithmetic Expression Tree with Minimum Parentheses](../1p3a-qbanks/waymo/raw/binary-expression-tree-serialize-min-parens.txt) | Waymo | Single report | hard |
| [Flatten / Unflatten Nested Python Structure](../1p3a-qbanks/xai/raw/flatten-unflatten-nested-structure.txt) | xAI | Single report | easy |
| [Radix Cache](../1p3a-qbanks/xai/raw/radix-cache.txt) | xAI | Single report | hard |

</details>

<details><summary><b>concurrency</b> (60)</summary>

| Question | Company | Freq | Level |
|---|---|---|---|
| [Coding Q1 — Image Processing Pipeline](../1p3a-qbanks/anthropic/raw/coding-q1-image-processing.txt) | Anthropic | Very high | medium |
| [Code Review — Multi-PR Walkthrough](../1p3a-qbanks/airbnb/raw/code-review-multi-pr.txt) | Airbnb | High | medium |
| [Coding Q1 — Concurrent Web Crawler](../1p3a-qbanks/anthropic/raw/coding-q1-web-crawler.txt) | Anthropic | High | medium |
| [Multi-Threaded Log Writer](../1p3a-qbanks/databricks/raw/multi-threaded-log-writer.txt) | Databricks | High | hard |
| [Customer Request PR Review](../1p3a-qbanks/jpmorgan/raw/customer-request-pr-review.txt) | JPMorgan Chase | High | medium |
| [CAP / DB Indexing / Concurrency / Logging Oral 八股](../1p3a-qbanks/bytedance/raw/backend-fundamentals-oral-cluster.txt) | ByteDance / TikTok | Medium | medium |
| [LRU Cache + Thread-Safe Follow-up](../1p3a-qbanks/linkedin/raw/coding-lru-cache-multithread.txt) | LinkedIn | Medium | medium |
| [Merge N-ary Trees by Node Key](../1p3a-qbanks/linkedin/raw/coding-merge-nary-trees-by-key.txt) | LinkedIn | Medium | medium |
| [LRU Cache (LC 146) + Multithreading Variant](../1p3a-qbanks/microsoft/raw/lru-cache.txt) | Microsoft | Medium | medium |
| [Weighted Cache / Timed Cache](../1p3a-qbanks/netflix/raw/weighted-cache-and-timed-cache.txt) | Netflix | Medium | medium |
| [Implement Retryer](../1p3a-qbanks/airbnb/raw/implement-retryer.txt) | Airbnb | Low | medium |
| [Rate Limiter Function and Distributed Design](../1p3a-qbanks/atlassian/raw/rate-limiter-function-and-distributed-design.txt) | Atlassian | Low | medium |
| [Sequential, Deduped, and Parallel URL Fetcher](../1p3a-qbanks/atlassian/raw/sequential-deduped-parallel-url-fetcher.txt) | Atlassian | Low | medium |
| [LRU Cache](../1p3a-qbanks/bloomberg/raw/lru-cache.txt) | Bloomberg | Low | medium |
| [Meeting Scheduler — Earliest Available Slot](../1p3a-qbanks/linkedin/raw/coding-meeting-scheduler-earliest-slot.txt) | LinkedIn | Low | medium |
| [Latency Tracker with Percentile Window](../1p3a-qbanks/netflix/raw/latency-tracker-percentile-window.txt) | Netflix | Low | medium |
| [Thread-Safe KV Store / Atomic Counter / Blocking Queue](../1p3a-qbanks/netflix/raw/thread-safe-kv-and-concurrency-basics.txt) | Netflix | Low | medium |
| [ModalLock and FairModalLock](../1p3a-qbanks/openai/raw/modal-lock-fair-modal-lock.txt) | OpenAI | Low | hard |
| [Time-Based Key-Value Store with Production Testing](../1p3a-qbanks/openai/raw/time-based-kv-store.txt) | OpenAI | Low | medium |
| [Thread-Safe Buy/Sell Without Overselling](../1p3a-qbanks/optiver/raw/multithreaded-buy-sell-lock.txt) | Optiver | Low | – |
| [Embedding Model Batching Service](../1p3a-qbanks/perplexity/raw/embedding-model-batching-service.txt) | Perplexity | Low | medium |
| [Project Architecture Deep Dive (AI Engineer / Backend)](../1p3a-qbanks/salesforce/raw/project-deep-dive-ai-engineer.txt) | Salesforce | Low | medium |
| [Transactional KV Store](../1p3a-qbanks/snowflake/raw/kv-store-transactions.txt) | Snowflake | Low | hard |
| [Multithreaded BankAccount Race-Condition Bug Hunt](../1p3a-qbanks/xai/raw/multithreaded-bank-account-bug-hunt.txt) | xAI | Low | easy |
| [Multithreaded Integer Sort](../1p3a-qbanks/xai/raw/parallel-integer-sort.txt) | xAI | Low | medium |
| [Process vs Thread (Verbal CS Fundamentals)](../1p3a-qbanks/xai/raw/process-vs-thread-verbal.txt) | xAI | Low | easy |
| [Hotel Booking System OOD Coding](../1p3a-qbanks/apple/raw/hotel-booking-system-ood-coding.txt) | Apple | Single report | medium |
| [OS Fundamentals & Concurrency Drill](../1p3a-qbanks/apple/raw/os-fundamentals-and-concurrency.txt) | Apple | Single report | medium |
| [SystemVerilog DV Constraint & Architecture Loop](../1p3a-qbanks/apple/raw/systemverilog-dv-constraint-and-architecture-loop.txt) | Apple | Single report | medium |
| [Write a Debounce Function](../1p3a-qbanks/apple/raw/write-a-debounce-function.txt) | Apple | Single report | medium |
| [RAG / Agent / Kafka Oral Drill](../1p3a-qbanks/bytedance/raw/rag-agent-kafka-oral-drill.txt) | ByteDance / TikTok | Single report | medium |
| [Single-Producer Multi-Consumer Ring Buffer](../1p3a-qbanks/citadel/raw/single-producer-multi-consumer-ring-buffer.txt) | Citadel / Citadel Securities | Single report | hard |
| [Thread-Safe Key Call Counter](../1p3a-qbanks/citadel/raw/thread-safe-key-call-counter.txt) | Citadel / Citadel Securities | Single report | medium |
| [Ring Buffer (Producer / Consumer with Backpressure)](../1p3a-qbanks/coinbase/raw/ring-buffer-producer-consumer.txt) | Coinbase | Single report | hard |
| [Implement Circuit Breaker Pattern](../1p3a-qbanks/databricks/raw/circuit-breaker-pattern.txt) | Databricks | Single report | medium |
| [Multi-Threaded Chat System](../1p3a-qbanks/databricks/raw/multi-threaded-chat-system.txt) | Databricks | Single report | medium |
| [Coding: First Unique Restaurant in a Stream](../1p3a-qbanks/doordash/raw/coding-first-unique-restaurant-stream.txt) | DoorDash | Single report | medium |
| [First Bad Version with Parallel Search Follow-up](../1p3a-qbanks/google/raw/first-bad-version-parallel-search.txt) | Google | Single report | medium |
| [Database Connection Pool](../1p3a-qbanks/harvey/raw/db-connection-pool.txt) | Harvey | Single report | medium |
| [Tally Service](../1p3a-qbanks/meta/raw/tally-service-concurrent-windowed-counter.txt) | Meta | Single report | medium |
| [Gen / Score Task Lock Scheduler](../1p3a-qbanks/microsoft/raw/gen-score-task-lock-scheduler.txt) | Microsoft | Single report | hard |
| [Idempotency API with Idempotency-Key Header](../1p3a-qbanks/microsoft/raw/idempotency-api-with-key.txt) | Microsoft | Single report | medium |
| [Multi-Key Bounded Task Executor](../1p3a-qbanks/microsoft/raw/multi-key-bounded-task-executor.txt) | Microsoft | Single report | hard |
| [C++ Project Debugging with AI Tools](../1p3a-qbanks/nvidia/raw/cpp-debugging-with-ai-tools.txt) | NVIDIA | Single report | hard |
| [Concurrent Simple Bank System](../1p3a-qbanks/okta/raw/concurrent-simple-bank-system.txt) | Okta | Single report | medium |
| [Network Access-Control Checker](../1p3a-qbanks/okta/raw/network-access-control-checker.txt) | Okta | Single report | medium |
| [Hospital Appointment Booking API](../1p3a-qbanks/oracle/raw/hospital-appointment-booking-api.txt) | Oracle | Single report | medium |
| [Chainable Rectangle Manipulation API](../1p3a-qbanks/robinhood/raw/chainable-rectangle-manipulation-api.txt) | Robinhood | Single report | – |
| [Security-Flavored Coding: Constrained Data Structure & Input Validation](../1p3a-qbanks/robinhood/raw/security-coding-constrained-ds-validation.txt) | Robinhood | Single report | – |
| [Onsite Mini Project — Concurrent Web Crawler](../1p3a-qbanks/salesforce/raw/web-crawler-mini-project.txt) | Salesforce | Single report | medium |
| [In-Memory Pub/Sub](../1p3a-qbanks/snapchat/raw/in-memory-pubsub.txt) | Snapchat | Single report | medium |
| [Rate Limiter](../1p3a-qbanks/snapchat/raw/rate-limiter.txt) | Snapchat | Single report | medium |
| [iOS Scrollable Grid & Swift Concurrency](../1p3a-qbanks/snapchat/raw/ios-scrollable-grid-and-swift-concurrency.txt) | Snapchat | Single report | medium |
| [Multi-Threaded Task Executor](../1p3a-qbanks/sofi/raw/multi-threaded-task-executor.txt) | SoFi | Single report | medium |
| [Rate Limiter](../1p3a-qbanks/stripe/raw/rate-limiter.txt) | Stripe | Single report | medium |
| [Event Bus with Emit, Subscribe, and Unsubscribe](../1p3a-qbanks/tesla/raw/event-bus-emit-subscribe.txt) | Tesla | Single report | medium |
| [Parallel Runner with Exclusive Targets A and B](../1p3a-qbanks/tesla/raw/parallel-target-runner.txt) | Tesla | Single report | medium |
| [Task Scheduler with Timed Execution and Dynamic Insertion](../1p3a-qbanks/tesla/raw/task-scheduler-timed-execution.txt) | Tesla | Single report | medium |
| [Implement a Rate Limiter](../1p3a-qbanks/waymo/raw/rate-limiter-implementation.txt) | Waymo | Single report | medium |
| [Distributed Token-Bucket Rate Limiter](../1p3a-qbanks/xai/raw/distributed-token-bucket-limiter.txt) | xAI | Single report | medium |

</details>

<details><summary><b>stack-queue</b> (52)</summary>

| Question | Company | Freq | Level |
|---|---|---|---|
| [Document / Layer — Apply, Undo, Commit Batch, Redo](../1p3a-qbanks/figma/raw/document-layer-apply-undo.txt) | Figma | High | medium |
| [Most Frequent Call Path from Function Trace Logs](../1p3a-qbanks/roblox/raw/most-frequent-call-path-from-trace-logs.txt) | Roblox | High | medium |
| [Exchange Order Matching Engine](../1p3a-qbanks/akunacapital/raw/exchange-order-matching-engine.txt) | Akuna Capital | Medium | medium |
| [Basic Calculator (with and without parentheses)](../1p3a-qbanks/bytedance/raw/basic-calculator.txt) | ByteDance / TikTok | Medium | hard |
| [Min Remove to Make Valid Parentheses](../1p3a-qbanks/meta/raw/min-remove-to-make-valid-parens.txt) | Meta | Medium | medium |
| [SWE / QSE OA — IPO Share Allocation](../1p3a-qbanks/twosigma/raw/swe-oa-ipo-share-allocation.txt) | Two Sigma | Medium | medium |
| [LeetCode 20 Valid Parentheses in SRE Loop](../1p3a-qbanks/bytedance/raw/valid-parentheses.txt) | ByteDance / TikTok | Low | – |
| [Remove Duplicate Letters for Lexicographically Smallest Result](../1p3a-qbanks/bytedance/raw/remove-duplicate-letters-lexicographically-smallest.txt) | ByteDance / TikTok | Low | medium |
| [Nested Pattern String Expansion](../1p3a-qbanks/cisco/raw/nested-pattern-string-expansion.txt) | Cisco | Low | medium |
| [Validate Brackets & Parse Nested Rich Text](../1p3a-qbanks/figma/raw/nested-brackets-rich-text-parse.txt) | Figma | Low | medium |
| [Process Starvation Time](../1p3a-qbanks/goldmansachs/raw/process-starvation-time.txt) | Goldman Sachs | Low | medium |
| [Decode String](../1p3a-qbanks/google/raw/decode-string.txt) | Google | Low | medium |
| [Nested Function Expression Evaluator](../1p3a-qbanks/google/raw/nested-function-expression-evaluator.txt) | Google | Low | medium |
| [Onsite Coding: Decode Password](../1p3a-qbanks/instacart/raw/onsite-decode-password.txt) | Instacart | Low | medium |
| [Function Inclusive / Exclusive Time](../1p3a-qbanks/linkedin/raw/coding-function-exclusive-inclusive-time.txt) | LinkedIn | Low | medium |
| [Basic Calculator I / II](../1p3a-qbanks/meta/raw/basic-calculator-i-ii.txt) | Meta | Low | medium |
| [Design a Queue — Implementation Trade-offs](../1p3a-qbanks/optiver/raw/design-queue-tradeoffs.txt) | Optiver | Low | – |
| [Decode String (k[encoded])](../1p3a-qbanks/oracle/raw/decode-string-lc394.txt) | Oracle | Low | medium |
| [Billing Status Replay (OOD)](../1p3a-qbanks/reddit/raw/billing-status-replay.txt) | Reddit | Low | medium |
| [Next Greater Element I](../1p3a-qbanks/pinduoduo/raw/next-greater-element-i.txt) | Temu / Pinduoduo | Low | easy |
| [Hit Counter](../1p3a-qbanks/affirm/raw/design-hit-counter.txt) | Affirm | Single report | easy |
| [Asteroid Collision (LC 735)](../1p3a-qbanks/amazon/raw/asteroid-collision-lc-735.txt) | Amazon | Single report | medium |
| [Basic Calculator with Postfix Notation](../1p3a-qbanks/amazon/raw/postfix-basic-calculator.txt) | Amazon | Single report | medium |
| [Maximum Frequency Stack](../1p3a-qbanks/amazon/raw/maximum-frequency-stack.txt) | Amazon | Single report | – |
| [Decode String](../1p3a-qbanks/apple/raw/decode-string.txt) | Apple | Single report | medium |
| [Valid Parentheses](../1p3a-qbanks/apple/raw/valid-parentheses.txt) | Apple | Single report | easy |
| [Candy Crush 1D](../1p3a-qbanks/bloomberg/raw/candy-crush-1d.txt) | Bloomberg | Single report | medium |
| [Decode String](../1p3a-qbanks/bloomberg/raw/decode-string.txt) | Bloomberg | Single report | medium |
| [Largest Rectangle in Histogram](../1p3a-qbanks/bytedance/raw/largest-rectangle-histogram.txt) | ByteDance / TikTok | Single report | hard |
| [Interleave Iterator (Round-Robin + Iterator Pattern)](../1p3a-qbanks/coinbase/raw/interleave-iterator.txt) | Coinbase | Single report | medium |
| [Code Craft: Basic Calculator (No Parentheses)](../1p3a-qbanks/doordash/raw/code-craft-basic-calculator-no-parens.txt) | DoorDash | Single report | medium |
| [Coding: Minimum Parenthesis Deletions](../1p3a-qbanks/doordash/raw/coding-minimum-parenthesis-deletions.txt) | DoorDash | Single report | easy |
| [Implement Python's itertools.tee](../1p3a-qbanks/google/raw/python-tee-independent-iterators.txt) | Google | Single report | medium |
| [Count Subarrays Where First = Last = Max](../1p3a-qbanks/linkedin/raw/coding-subarray-first-last-equal-max.txt) | LinkedIn | Single report | medium |
| [One-Move Balanced Parentheses](../1p3a-qbanks/mathworks/raw/one-move-balanced-parentheses.txt) | MathWorks | Single report | medium |
| [Minimum Add to Make Parentheses Valid](../1p3a-qbanks/meta/raw/minimum-add-to-make-valid-parens.txt) | Meta | Single report | easy |
| [Basic Calculator](../1p3a-qbanks/netflix/raw/basic-calculator.txt) | Netflix | Single report | hard |
| [Reverse Substrings Between Each Pair of Parentheses](../1p3a-qbanks/okta/raw/reverse-substrings-in-parentheses.txt) | Okta | Single report | medium |
| [Fault-Tolerant Work Queue](../1p3a-qbanks/openai/raw/fault-tolerant-work-queue.txt) | OpenAI | Single report | medium |
| [Implement a CD Command](../1p3a-qbanks/openai/raw/implement-cd-command.txt) | OpenAI | Single report | medium |
| [Design Circular Queue (Ring Buffer)](../1p3a-qbanks/optiver/raw/circular-queue-design.txt) | Optiver | Single report | easy |
| [Simplify Expression by Removing Parentheses](../1p3a-qbanks/oracle/raw/simplify-parentheses-expression.txt) | Oracle | Single report | medium |
| [Design Circular Queue](../1p3a-qbanks/snowflake/raw/design-circular-queue.txt) | Snowflake | Single report | medium |
| [String-Command Calculator](../1p3a-qbanks/snowflake/raw/calculator-string-commands.txt) | Snowflake | Single report | medium |
| [Valid Parentheses](../1p3a-qbanks/snowflake/raw/valid-parentheses.txt) | Snowflake | Single report | easy |
| [Basic Calculator with Operators, Variables, and Functions](../1p3a-qbanks/tesla/raw/basic-calculator-extended-language.txt) | Tesla | Single report | medium |
| [Design Hit Counter](../1p3a-qbanks/uber/raw/hit-counter-design.txt) | Uber | Single report | medium |
| [Exclusive Time of Functions](../1p3a-qbanks/uber/raw/exclusive-time-of-functions.txt) | Uber | Single report | medium |
| [OOD Counter Class](../1p3a-qbanks/uber/raw/ood-counter-class.txt) | Uber | Single report | medium |
| [Plants Pesticide — Days Until Stable](../1p3a-qbanks/walmartlabs/raw/plants-pesticide-days.txt) | Walmart Labs | Single report | hard |
| [Decode String with `(group){k}` Repeat Syntax](../1p3a-qbanks/waymo/raw/decode-string-curly-repeat.txt) | Waymo | Single report | medium |
| [Number of Visible People in a Queue (LC 1944)](../1p3a-qbanks/waymo/raw/number-of-visible-people-in-a-queue-lc-1944.txt) | Waymo | Single report | hard |

</details>

<details><summary><b>other</b> (50)</summary>

| Question | Company | Freq | Level |
|---|---|---|---|
| [Technical Deep Dive (slide-based)](../1p3a-qbanks/openai/raw/technical-deep-dive.txt) | OpenAI | Very high | – |
| [Zap-N Reaction & Memory Mini-Games](../1p3a-qbanks/optiver/raw/zap-n-reaction-games.txt) | Optiver | Very high | – |
| [SRE Linux / Networking Troubleshooting Round](../1p3a-qbanks/bytedance/raw/sre-linux-networking-troubleshoot.txt) | ByteDance / TikTok | High | medium |
| [Onsite: Project Deep Dive](../1p3a-qbanks/doordash/raw/onsite-project-deep-dive.txt) | DoorDash | High | medium |
| [Google Hiring Assessment (GHA / Work Styles)](../1p3a-qbanks/google/raw/google-hiring-assessment-gha.txt) | Google | High | – |
| [Project Deep Dive](../1p3a-qbanks/harvey/raw/project-deep-dive.txt) | Harvey | High | – |
| [AI Application Conversation](../1p3a-qbanks/intuit/raw/ai-application-conversation.txt) | Intuit | High | – |
| [Verbal Java / CS-Fundamentals Quiz](../1p3a-qbanks/oracle/raw/verbal-java-cs-fundamentals.txt) | Oracle | High | – |
| [Project Deep Dive](../1p3a-qbanks/robinhood/raw/project-deep-dive.txt) | Robinhood | High | – |
| [Full-Stack Craft: Login and Upload Flow](../1p3a-qbanks/atlassian/raw/fullstack-craft-login-and-upload-flow.txt) | Atlassian | Medium | medium |
| [Onsite: On-Call Case Investigation](../1p3a-qbanks/doordash/raw/onsite-case-investigation.txt) | DoorDash | Medium | medium |
| [Programming & Computer-System Knowledge Quiz](../1p3a-qbanks/optiver/raw/coding-knowledge-test.txt) | Optiver | Medium | – |
| [QR Take-Home Data Project & Presentation](../1p3a-qbanks/optiver/raw/qr-take-home-data-project.txt) | Optiver | Medium | – |
| [Resume / Project Deep Dive (HM or Tech Screen Opener)](../1p3a-qbanks/oracle/raw/resume-project-deep-dive.txt) | Oracle | Medium | – |
| [Project Deep Dive](../1p3a-qbanks/vanta/raw/project-deep-dive.txt) | Vanta | Medium | – |
| [Accessories QA Benchmark and Regression Deep Dive](../1p3a-qbanks/apple/raw/accessories-qa-benchmark-and-regression-deep-dive.txt) | Apple | Low | – |
| [Code Reading Pipeline Walkthrough](../1p3a-qbanks/capitalone/raw/code-reading-pipeline-walkthrough.txt) | Capital One | Low | medium |
| [PE Troubleshooting — Web Server / Disk Full](../1p3a-qbanks/meta/raw/pe-troubleshooting.txt) | Meta | Low | medium |
| [Hardware-Adjacent Project Deep Dive](../1p3a-qbanks/nvidia/raw/hardware-adjacent-project-deep-dive.txt) | NVIDIA | Low | medium |
| [Data Engineering Movie Success Pipeline](../1p3a-qbanks/netflix/raw/data-engineering-movie-success-pipeline.txt) | Netflix | Low | medium |
| [Cloud / Domain Knowledge Oral Round (GCP / AWS / K8s)](../1p3a-qbanks/salesforce/raw/cloud-domain-knowledge-round.txt) | Salesforce | Low | medium |
| [Resume-Driven Language & Database Fundamentals](../1p3a-qbanks/pinduoduo/raw/resume-driven-language-database-fundamentals.txt) | Temu / Pinduoduo | Low | – |
| [AI-Assisted Full-Stack OA (Spring Boot + React)](../1p3a-qbanks/walmartlabs/raw/ai-assisted-fullstack-oa.txt) | Walmart Labs | Low | – |
| [Data Fluency: Self-Driving Progress Metrics & Experiment Comparison](../1p3a-qbanks/waymo/raw/data-fluency-progress-metrics.txt) | Waymo | Low | hard |
| [Junior Trader Final: Investment & Market-Making Games](../1p3a-qbanks/akunacapital/raw/junior-trader-final-games.txt) | Akuna Capital | Single report | medium |
| [Applied Science Manager Phone Screen — Project Deep-Dive + Leadership BQ](../1p3a-qbanks/amazon/raw/asm-phone-screen-deep-dive-bq.txt) | Amazon | Single report | – |
| [MLP Compute-vs-Memory Bound Analysis](../1p3a-qbanks/amazon/raw/mlp-compute-memory-bound-analysis.txt) | Amazon | Single report | medium |
| [Robot Human-Avoidance — Open-Ended Design](../1p3a-qbanks/amazon/raw/robot-human-avoidance-open-design.txt) | Amazon | Single report | hard |
| [Mechanistic Interpretability Take-Home — Sample-Aspect Double Descent](../1p3a-qbanks/anthropic/raw/mech-interp-take-home.txt) | Anthropic | Single report | hard |
| [ML Systems Codebase Deep Dive](../1p3a-qbanks/apple/raw/ml-systems-codebase-deep-dive.txt) | Apple | Single report | medium |
| [AI Agent Intern Technical Questions](../1p3a-qbanks/bytedance/raw/ai-agent-intern-oral-fundamentals.txt) | ByteDance / TikTok | Single report | – |
| [LLM Safety Project Deep Dive](../1p3a-qbanks/bytedance/raw/llm-safety-project-deep-dive.txt) | ByteDance / TikTok | Single report | – |
| [AR Panel Talk Project Deep-Dive](../1p3a-qbanks/capitalone/raw/ar-panel-talk-deep-dive.txt) | Capital One | Single report | – |
| [Amusement Park Profitability Case](../1p3a-qbanks/capitalone/raw/amusement-park-profitability-case.txt) | Capital One | Single report | medium |
| [Chatbot Case Study](../1p3a-qbanks/capitalone/raw/chatbot-case-study.txt) | Capital One | Single report | – |
| [Wheat Harvest Optimisation Case](../1p3a-qbanks/capitalone/raw/wheat-harvest-case.txt) | Capital One | Single report | medium |
| [Deep Dive — What Happens When You Access a URL](../1p3a-qbanks/figma/raw/deep-dive-url-request-lifecycle.txt) | Figma | Single report | – |
| [CPU Architecture and Simulator Design Deep Dive](../1p3a-qbanks/google/raw/cpu-architecture-simulator-deep-dive.txt) | Google | Single report | hard |
| [Research Paper Presentation and Defense (DeepMind RS)](../1p3a-qbanks/google/raw/deepmind-rs-paper-presentation-defense.txt) | Google | Single report | hard |
| [DevSecOps Kubernetes / CI-CD Technical Screen](../1p3a-qbanks/ibm/raw/devsecops-kubernetes-cicd-screen.txt) | IBM | Single report | – |
| [Staff Project Architecture and Delivery Deep Dive](../1p3a-qbanks/intuit/raw/staff-project-architecture-delivery-deep-dive.txt) | Intuit | Single report | – |
| [Agentic Portfolio Workflow Deep Dive](../1p3a-qbanks/jpmorgan/raw/agentic-portfolio-workflow-deep-dive.txt) | JPMorgan Chase | Single report | – |
| [Data Platform, Pipeline, and ML Operations Fundamentals](../1p3a-qbanks/nvidia/raw/data-platform-pipeline-and-ml-operations-fundamentals.txt) | NVIDIA | Single report | medium |
| [Market-Making Interval Estimation Game](../1p3a-qbanks/optiver/raw/market-making-interval-game.txt) | Optiver | Single report | – |
| [Code Review Round (Verbal)](../1p3a-qbanks/oracle/raw/code-review-verbal.txt) | Oracle | Single report | medium |
| [Data-Center Camera Access Incident Response](../1p3a-qbanks/oracle/raw/data-center-camera-access-incident-response.txt) | Oracle | Single report | – |
| [Product Sense: Improve Reddit Onboarding](../1p3a-qbanks/reddit/raw/product-sense-improve-onboarding.txt) | Reddit | Single report | medium |
| [GenAI Quant SWE Manager Deep Dive](../1p3a-qbanks/twosigma/raw/genai-quant-swe-manager-deep-dive.txt) | Two Sigma | Single report | medium |
| [Spring Boot Verbal Quiz](../1p3a-qbanks/walmartlabs/raw/spring-boot-verbal-quiz.txt) | Walmart Labs | Single report | – |
| [SWE Resume Deep Dive: Technologies and Outcome Metrics](../1p3a-qbanks/waymo/raw/swe-resume-deep-dive-technologies-metrics.txt) | Waymo | Single report | medium |

</details>

<details><summary><b>linked-list</b> (40)</summary>

| Question | Company | Freq | Level |
|---|---|---|---|
| [Escape Room / Room-by-Room Race](../1p3a-qbanks/pinterest/raw/escape-room-player-tracking.txt) | Pinterest | High | medium |
| [Popular Content Counter](../1p3a-qbanks/atlassian/raw/popular-content-all-o-one.txt) | Atlassian | Medium | medium |
| [LRU Cache (with TTL and LFU Follow-ups)](../1p3a-qbanks/bytedance/raw/lru-cache-with-ttl.txt) | ByteDance / TikTok | Medium | medium |
| [Reverse Nodes in K-Group (incomplete tail variant)](../1p3a-qbanks/bytedance/raw/reverse-nodes-in-k-group.txt) | ByteDance / TikTok | Medium | hard |
| [Command Executor with Undo and Tags](../1p3a-qbanks/netflix/raw/command-executor-with-undo-and-tags.txt) | Netflix | Medium | medium |
| [LRU Cache](../1p3a-qbanks/shopify/raw/lru-cache.txt) | Shopify | Medium | medium |
| [LRU Cache with TTL or Weighted Size](../1p3a-qbanks/snapchat/raw/lru-cache-with-ttl-or-weighted-size.txt) | Snapchat | Medium | medium |
| [Linked-List Intersection With Cycles](../1p3a-qbanks/airbnb/raw/linked-list-intersection-with-cycles.txt) | Airbnb | Low | medium |
| [LFU Cache](../1p3a-qbanks/linkedin/raw/coding-lfu-cache.txt) | LinkedIn | Low | hard |
| [Max Stack](../1p3a-qbanks/linkedin/raw/coding-max-stack.txt) | LinkedIn | Low | medium |
| [LRU Cache with Expiration (LC 146 + TTL)](../1p3a-qbanks/oracle/raw/lru-cache-with-expiration.txt) | Oracle | Low | medium |
| [LFU Cache (LeetCode 460)](../1p3a-qbanks/salesforce/raw/lfu-cache.txt) | Salesforce | Low | hard |
| [Singly Linked List — Remove Duplicate Values](../1p3a-qbanks/salesforce/raw/linked-list-remove-duplicates.txt) | Salesforce | Low | easy |
| [HashMap from Scratch](../1p3a-qbanks/twosigma/raw/hashmap-from-scratch.txt) | Two Sigma | Low | medium |
| [Detect and Break a Linked-List Cycle](../1p3a-qbanks/amazon/raw/linked-list-detect-break-cycle.txt) | Amazon | Single report | medium |
| [Manual Queue Implementation](../1p3a-qbanks/amazon/raw/manual-queue-implementation.txt) | Amazon | Single report | – |
| [Copy List with Random Pointer (LC 138)](../1p3a-qbanks/apple/raw/copy-list-with-random-pointer.txt) | Apple | Single report | medium |
| [Intersection of Two Linked Lists](../1p3a-qbanks/apple/raw/intersection-of-two-linked-lists.txt) | Apple | Single report | easy |
| [LFU Cache](../1p3a-qbanks/apple/raw/lfu-cache.txt) | Apple | Single report | hard |
| [LRU Cache](../1p3a-qbanks/apple/raw/lru-cache.txt) | Apple | Single report | medium |
| [Merge Two Sorted Lists](../1p3a-qbanks/apple/raw/merge-two-sorted-lists.txt) | Apple | Single report | easy |
| [Odd Even Linked List](../1p3a-qbanks/apple/raw/odd-even-linked-list.txt) | Apple | Single report | medium |
| [Todo List OOP Design](../1p3a-qbanks/bloomberg/raw/todo-list-oop.txt) | Bloomberg | Single report | medium |
| [Reverse a Linked List In Place](../1p3a-qbanks/bytedance/raw/reverse-linked-list-in-place.txt) | ByteDance / TikTok | Single report | – |
| [Add Two Reversed Digit Lists](../1p3a-qbanks/cisco/raw/add-two-reversed-digit-lists.txt) | Cisco | Single report | easy |
| [Linked List Binary to Decimal](../1p3a-qbanks/cisco/raw/linked-list-binary-to-decimal.txt) | Cisco | Single report | easy |
| [LRU + LFU + Custom Eviction Function](../1p3a-qbanks/citadel/raw/lru-lfu-custom-evict.txt) | Citadel / Citadel Securities | Single report | medium |
| [Code Craft: Eligible Orders / Neighbor-Check Sweep](../1p3a-qbanks/doordash/raw/code-craft-eligible-orders-sweep.txt) | DoorDash | Single report | medium |
| [Implement a Deque](../1p3a-qbanks/goldmansachs/raw/implement-deque.txt) | Goldman Sachs | Single report | easy |
| [LRU Cache](../1p3a-qbanks/meta/raw/lru-cache.txt) | Meta | Single report | medium |
| [Remove Nth Node From End of List](../1p3a-qbanks/meta/raw/lc-remove-nth-from-end.txt) | Meta | Single report | easy |
| [Contiguous Memory Allocator](../1p3a-qbanks/microsoft/raw/memory-allocator.txt) | Microsoft | Single report | medium |
| [Palindrome Linked List (LC 234) + Engineering Follow-Up](../1p3a-qbanks/microsoft/raw/palindrome-linked-list.txt) | Microsoft | Single report | medium |
| [First-Login Only-Once User Tracker (O(1) Worst-Case)](../1p3a-qbanks/oracle/raw/first-unique-login-tracker.txt) | Oracle | Single report | medium |
| [Odd Even Linked List](../1p3a-qbanks/reddit/raw/odd-even-linked-list.txt) | Reddit | Single report | medium |
| [Copy List with Random Pointer](../1p3a-qbanks/snowflake/raw/copy-list-with-random-pointer.txt) | Snowflake | Single report | medium |
| [Merge Two Sorted Lists](../1p3a-qbanks/snowflake/raw/merge-two-sorted-lists.txt) | Snowflake | Single report | easy |
| [Key-Value Store with getLast](../1p3a-qbanks/sofi/raw/key-value-store-with-getlast.txt) | SoFi | Single report | medium |
| [LRU Cache](../1p3a-qbanks/pinduoduo/raw/lru-cache.txt) | Temu / Pinduoduo | Single report | medium |
| [Weighted LRU Cache](../1p3a-qbanks/xai/raw/weighted-lru-cache.txt) | xAI | Single report | medium |

</details>

<details><summary><b>binary-search</b> (35)</summary>

| Question | Company | Freq | Level |
|---|---|---|---|
| [QR OA — Linear Interpolator](../1p3a-qbanks/twosigma/raw/qr-oa-linear-interpolator.txt) | Two Sigma | High | medium |
| [Weighted Sampling from a Probability Distribution](../1p3a-qbanks/linkedin/raw/coding-weighted-sampling-die.txt) | LinkedIn | Medium | medium |
| [Find Peak Element](../1p3a-qbanks/meta/raw/find-peak-element.txt) | Meta | Medium | medium |
| [Random Pick with Weight](../1p3a-qbanks/meta/raw/random-pick-with-weight.txt) | Meta | Medium | medium |
| [Resilient / Versioned In-Memory File System](../1p3a-qbanks/netflix/raw/resilient-versioned-file-system.txt) | Netflix | Medium | medium |
| [Banking — Deposit / Withdraw / Transaction / Balance](../1p3a-qbanks/airbnb/raw/banking-transactions-class.txt) | Airbnb | Low | easy |
| [Weighted Random Load Balancer IP](../1p3a-qbanks/apple/raw/weighted-random-load-balancer-ip.txt) | Apple | Low | medium |
| [Search in Rotated Sorted Array](../1p3a-qbanks/bytedance/raw/search-in-rotated-sorted-array.txt) | ByteDance / TikTok | Low | medium |
| [Time-Based Key-Value Store (LC 981)](../1p3a-qbanks/oracle/raw/time-based-key-value-store-lc981.txt) | Oracle | Low | medium |
| [Binary Search Log Entries by Date String](../1p3a-qbanks/pinterest/raw/binary-search-log-by-date.txt) | Pinterest | Low | easy |
| [Flight Tracker — User Location at a Given Time](../1p3a-qbanks/ramp/raw/flight-user-location.txt) | Ramp | Low | medium |
| [Median of Two Sorted Arrays](../1p3a-qbanks/rippling/raw/median-of-two-sorted-arrays.txt) | Rippling | Low | hard |
| [Event Stream Count in Time Range](../1p3a-qbanks/snowflake/raw/event-stream-count-range.txt) | Snowflake | Low | medium |
| [Closest Version Date](../1p3a-qbanks/amazon/raw/closest-version-date.txt) | Amazon | Single report | medium |
| [Find Median (Oracle Variant)](../1p3a-qbanks/apple/raw/find-median-oracle-variant.txt) | Apple | Single report | hard |
| [Koko Eating Bananas](../1p3a-qbanks/apple/raw/koko-eating-bananas.txt) | Apple | Single report | medium |
| [Elements Appearing More Than n/3 in a Sorted Array](../1p3a-qbanks/bytedance/raw/sorted-array-elements-more-than-one-third.txt) | ByteDance / TikTok | Single report | medium |
| [Train Schedule Nearest Departure](../1p3a-qbanks/capitalone/raw/train-schedule-nearest-departure.txt) | Capital One | Single report | easy |
| [Time-Keyed Key-Value Store (set / get-before-timestamp)](../1p3a-qbanks/citadel/raw/time-keyed-kv-store.txt) | Citadel / Citadel Securities | Single report | medium |
| [Weighted Random Sampling with Insert / Delete](../1p3a-qbanks/citadel/raw/weighted-sampling-with-updates.txt) | Citadel / Citadel Securities | Single report | hard |
| [Coding: Minimum Dasher Processing Speed](../1p3a-qbanks/doordash/raw/coding-minimum-dasher-processing-speed.txt) | DoorDash | Single report | medium |
| [Cutting Ribbons (LC 1891)](../1p3a-qbanks/google/raw/cutting-ribbons-max-equal-length.txt) | Google | Single report | medium |
| [Count Distinct Values in a Massive Sorted Array](../1p3a-qbanks/linkedin/raw/coding-count-distinct-sorted-array.txt) | LinkedIn | Single report | medium |
| [Binary Search — Rightmost Index of Duplicate](../1p3a-qbanks/oracle/raw/binary-search-rightmost-duplicate.txt) | Oracle | Single report | easy |
| [KV Store with Timestamps and Restore](../1p3a-qbanks/perplexity/raw/kv-store-with-timestamps-and-restore.txt) | Perplexity | Single report | medium |
| [Event Count Query over Timestamp Range](../1p3a-qbanks/snapchat/raw/event-count-query-over-timestamp-range.txt) | Snapchat | Single report | medium |
| [Split Array Largest Sum (LC 410)](../1p3a-qbanks/snapchat/raw/split-array-largest-sum.txt) | Snapchat | Single report | hard |
| [Trajectory Waypoint Distance Queries](../1p3a-qbanks/tesla/raw/trajectory-waypoint-distance-queries.txt) | Tesla | Single report | medium |
| [Last-Click Attribution Tracker](../1p3a-qbanks/uber/raw/last-click-attribution.txt) | Uber | Single report | medium |
| [Median of Two Sorted Arrays](../1p3a-qbanks/uber/raw/median-of-two-sorted-arrays.txt) | Uber | Single report | hard |
| [Sqrt(x)](../1p3a-qbanks/uber/raw/sqrt-x.txt) | Uber | Single report | easy |
| [Time Based Key-Value Store](../1p3a-qbanks/uber/raw/time-based-key-value-store.txt) | Uber | Single report | medium |
| [Run-Length Encoded String: Find by Index and Range Max](../1p3a-qbanks/waymo/raw/run-length-encoding-find-by-index.txt) | Waymo | Single report | medium |
| [Two-Column Table: Place Divider to Minimize Total Height](../1p3a-qbanks/waymo/raw/two-column-table-optimal-divider.txt) | Waymo | Single report | medium |
| [GPU Node Group Test — Identify Bad Nodes](../1p3a-qbanks/xai/raw/gpu-node-group-test.txt) | xAI | Single report | medium |

</details>

<details><summary><b>array-string-misc</b> (29)</summary>

| Question | Company | Freq | Level |
|---|---|---|---|
| [IP Address / CIDR Iterator (5-part)](../1p3a-qbanks/openai/raw/ip-address-cidr-iterator.txt) | OpenAI | Medium | medium |
| [Transaction Fee Calculator](../1p3a-qbanks/stripe/raw/transaction-fee-calculator.txt) | Stripe | Medium | easy |
| [Branch Merge Minimum Conflicts](../1p3a-qbanks/amazon/raw/branch-merge-minimum-conflicts.txt) | Amazon | Low | – |
| [Symmetric Triplets](../1p3a-qbanks/capitalone/raw/symmetric-triplets.txt) | Capital One | Low | easy |
| [Look-and-Say Next Sequence](../1p3a-qbanks/cisco/raw/look-and-say-next-sequence.txt) | Cisco | Low | easy |
| [Count Palindromic Substrings](../1p3a-qbanks/goldmansachs/raw/palindromic-substrings.txt) | Goldman Sachs | Low | medium |
| [Minimum Round-Trip Flight Cost](../1p3a-qbanks/meta/raw/minimum-round-trip-flight-cost.txt) | Meta | Low | medium |
| [Maximum-Value Frequency in Array Suffixes](../1p3a-qbanks/microsoft/raw/max-number-occurrences-in-suffix.txt) | Microsoft | Low | easy |
| [Best Time to Buy and Sell Stock — Single Transaction](../1p3a-qbanks/oracle/raw/single-transaction-stock-profit.txt) | Oracle | Low | easy |
| [Dataset Validation (CSV with banned + stop words)](../1p3a-qbanks/stripe/raw/dataset-validation-csv.txt) | Stripe | Low | easy |
| [Merchant Fraud Score (OA)](../1p3a-qbanks/stripe/raw/merchant-fraud-score-oa.txt) | Stripe | Low | medium |
| [Request Replay Integration](../1p3a-qbanks/stripe/raw/request-replay-integration.txt) | Stripe | Low | medium |
| [OA: Final Prices with Special Discount (Sum + Unsold Indices)](../1p3a-qbanks/uber/raw/oa-final-prices-monotonic-stack.txt) | Uber | Low | medium |
| [Count Set Bits in an Integer Array](../1p3a-qbanks/akunacapital/raw/count-set-bits-in-integer-array.txt) | Akuna Capital | Single report | easy |
| [Binary Array Rearrangement](../1p3a-qbanks/amazon/raw/binary-array-minimum-adjacent-swaps.txt) | Amazon | Single report | – |
| [Get Nested Object by Path](../1p3a-qbanks/amazon/raw/get-nested-object-by-path.txt) | Amazon | Single report | medium |
| [Longest Substring Without Repeating Characters](../1p3a-qbanks/amazon/raw/longest-substring-without-repeating-characters.txt) | Amazon | Single report | – |
| [Stickers to Spell Word (LeetCode 691)](../1p3a-qbanks/bytedance/raw/stickers-to-spell-word.txt) | ByteDance / TikTok | Single report | hard |
| [Even Digit Count](../1p3a-qbanks/capitalone/raw/even-digit-count.txt) | Capital One | Single report | easy |
| [Refactor Obfuscated C++ Code (Hidden Best-Time-To-Buy-Sell)](../1p3a-qbanks/citadel/raw/obfuscated-code-refactor.txt) | Citadel / Citadel Securities | Single report | easy |
| [First Missing Positive](../1p3a-qbanks/goldmansachs/raw/first-missing-positive.txt) | Goldman Sachs | Single report | hard |
| [Snake Case to Camel Case with Repository-Wide Refactor](../1p3a-qbanks/google/raw/snake-case-repository-refactor.txt) | Google | Single report | medium |
| [String Decompression with Nested Counts](../1p3a-qbanks/google/raw/string-decompression-nested-counts.txt) | Google | Single report | medium |
| [Look-and-Say Sequence](../1p3a-qbanks/intuit/raw/look-and-say-sequence.txt) | Intuit | Single report | easy |
| [First Missing Positive](../1p3a-qbanks/netflix/raw/first-missing-positive.txt) | Netflix | Single report | hard |
| [New-Grad CodeSignal OA](../1p3a-qbanks/pinterest/raw/code-signal-ng-oa.txt) | Pinterest | Single report | medium |
| [Word Sequence Validation](../1p3a-qbanks/reddit/raw/word-sequence-validation.txt) | Reddit | Single report | – |
| [URL / Path Segment Compression](../1p3a-qbanks/stripe/raw/url-segment-compression.txt) | Stripe | Single report | medium |
| [Merge Strings Alternately](../1p3a-qbanks/walmartlabs/raw/merge-strings-alternately.txt) | Walmart Labs | Single report | easy |

</details>
