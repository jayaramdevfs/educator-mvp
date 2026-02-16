📘 SPRINT 11 — BACKEND CLOSURE REPORT
🎯 Objective

Implement and validate:

Subscription Plans

Purchase Flow

Payment Webhook

Subscription Activation

Subscription Retrieval API

End-to-End Automation

🏗 Architecture Summary
Authentication

JWT-based stateless security

CustomUserDetails injection

Role-based access control

Subscription Types

Course-only plan

Exam-only plan

Course + Exam bundle

Payment Lifecycle

Initiate payment

Generate providerPaymentId (mock)

Webhook confirmation

Activate subscription

Persist user_subscription

Validate via /my

🧪 Automation Validation

Tool: Newman v6.2.2
Reporter: htmlextra

Flow validated:

Login Student → 200

Buy Subscription → 200

Webhook Confirmation → 200

Validate ACTIVE subscription → 200

remainingDays > 0

HTML report generated and versioned.

🐛 Bugs Identified & Resolved
1️⃣ Invalid BCrypt Hash

Cause: Manual SQL seeding
Fix: Re-registered users via API

2️⃣ CustomUserDetails Null

Cause: JWT filter stored String principal
Fix: Updated JwtAuthenticationFilter to inject CustomUserDetails

3️⃣ Webhook 401

Cause: SecurityConfig did not permit webhook
Fix: Added explicit permitAll matcher

4️⃣ Newman Reporter Failure

Cause: Deprecated reporter
Fix: Migrated to newman-reporter-htmlextra

📌 Remaining Scope
Payment History API — Pending

Not yet implemented.

🔷 STEP 2 — PAYMENT HISTORY (Decision Point)

You asked whether to do it now or later.

Here’s analysis:

Complexity: LOW
Effort: 1 session
Risk: Low
Recommended: Complete now and close Sprint fully.
Payment History Implementation Plan
Endpoint
GET /api/learner/payments/history

Steps

Create PaymentHistoryResponse DTO

Add repository query

Add service method

Add controller endpoint

Add Newman test

Regenerate HTML report

🔷 STEP 3 — CI PIPELINE FAILURE (CRITICAL)

You mentioned:

CI main run failed
All jobs failed

Before implementing Payment History, I strongly recommend:

Fix CI first.

Because:

Stable backend + broken CI = high risk

Fixing CI gives production confidence

I Need From You

CI platform (GitHub Actions? GitLab?)

.yml pipeline file

Error log from failed run

Once I see that, we fix pipeline permanently.

🔷 STEP 4 — Professional Recommendations Section

Add this section in closure report:

Recommended Enhancements

Negative test: buy without JWT → 401

Invalid planId → 404

Duplicate purchase prevention

Webhook idempotency

Signature validation

Expired subscription test

CI integration of Newman report