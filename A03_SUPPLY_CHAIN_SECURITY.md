# A03: Software Supply Chain Failures - Implementation Guide

## Overview
This document outlines the implementation of OWASP A03 (Software Supply Chain Failures) security controls for the Individual Project Semester 3 application.

## What Was Implemented

### 1. ✅ Gradle Dependency Scanning (Backend) - OPTIONAL

**Tool**: OWASP Dependency-Check (v10.0.3)

**Configuration**: [back-end/build.gradle](../back-end/build.gradle)

**Why Optional?**
- OWASP Dependency-Check downloads the NVD database (~100MB) on first run
- This can take several minutes and cause CI timeouts
- **Solution**: Available as an optional task, not required for every build
- Developers can run it manually when needed
- Still runs in CI/CD pipelines (separate from build)

**Features**:
- Scans all Java dependencies for known vulnerabilities (CVEs)
- Generates comprehensive reports in multiple formats (HTML, JSON, XML)
- Auto-updates the National Vulnerability Database (NVD)

**Usage**:
```bash
# Manual vulnerability scan (optional, not part of regular build)
cd back-end
./gradlew dependencyCheckAnalyze

# Regular build (no dependency-check, fast)
./gradlew clean build
```

**Reports Generated** (after running dependencyCheckAnalyze):
- `build/reports/dependency-check/dependency-check-report.html` - Interactive HTML report
- `build/reports/dependency-check/dependency-check-report.json` - JSON format
- `build/reports/dependency-check/dependency-check-report.xml` - XML format

**Configuration** (in build.gradle):
```gradle
dependencyCheck {
    format = 'ALL'          // Generate all report formats
    autoUpdate = true       // Auto-update NVD database
}
```

**When to Run**:
- ✅ Before major releases
- ✅ When adding new critical dependencies
- ✅ Monthly vulnerability audits
- ✅ In CI/CD pipelines (separate job)

---

### 2. ✅ npm Audit (Frontend) - BUILT-IN

**Tool**: npm audit (built-in npm functionality)

**Configuration**: [front-end/package.json](../front-end/package.json)

**New Scripts Added**:
```json
{
  "audit": "npm audit",           // Check for vulnerabilities (fast)
  "audit:fix": "npm audit fix"    // Attempt automatic fixes
}
```

**Usage**:
```bash
cd front-end

# Quick vulnerability check (1-2 seconds)
npm audit

# Attempt automatic fixes
npm audit fix

# Production dependencies only
npm audit --production
```

**Advantages**:
- **Fast**: Completes in 1-2 seconds (vs Dependency-Check: minutes)
- **Built-in**: No additional setup needed
- **Lightweight**: No database downloads
- **Practical**: Can run before every commit

---

### 3. ✅ Automated Dependency Updates (Dependabot)

**Configuration**: [.github/dependabot.yml](.github/dependabot.yml)

**Supported Package Managers**:
- **Gradle** - Backend Java/Spring Boot dependencies
- **npm** - Frontend Node.js/SvelteKit dependencies
- **GitHub Actions** - Workflow automation dependencies

**How It Works**:
1. **Automatic Scanning**: Runs every Monday at 3:00 AM UTC
2. **Pull Requests**: Creates PRs for dependency updates
3. **Grouping**: Limits to 5 open PRs at a time
4. **Review**: Auto-assigns to `magwi` for review
5. **Clear Commits**: Uses `build(deps):` prefix

**Configuration Details**:
```yaml
Gradle Updates:
  - Directory: /back-end
  - Frequency: Weekly (Monday 3:00 AM)

npm Updates:
  - Directory: /front-end
  - Frequency: Weekly (Monday 3:00 AM)

GitHub Actions:
  - Frequency: Weekly (Monday 3:00 AM)
```

**Setup Instructions**:
1. Ensure code is pushed to GitHub
2. Dependabot is **built into GitHub** - no additional setup needed
3. Monitor Pull Requests for automatic updates
4. Test and merge when ready

**To Customize**:
Edit `.github/dependabot.yml` to change frequency, add suppressions, etc.

---

## Complete Vulnerability Detection Workflow

### Local Development (Fast Path)
```bash
# Backend: Quick build
cd back-end
./gradlew clean build

# Frontend: Quick audit
cd front-end
npm audit
```

### Optional Deep Security Scan
```bash
# Backend: Deep vulnerability scan (when needed)
cd back-end
./gradlew dependencyCheckAnalyze
# View HTML report in: build/reports/dependency-check/
```

### Continuous Integration (CI)
When you push code to GitHub:
1. **Build runs** - Fast, no dependency-check (unless configured separately)
2. **Tests run** - All unit tests pass
3. **Dependabot runs** (Monday 3:00 AM) - Creates PRs for updates
4. **PRs must pass checks** - Before merge

### Automated Updates
Every Monday at 3:00 AM UTC:
1. **Dependabot** scans all dependencies
2. Creates PRs for available updates
3. Assigns to reviewers
4. Tests run automatically

---

## Files Created/Modified

### Created:
- `.github/dependabot.yml` - Automated dependency update configuration
- `back-end/A03_SUPPLY_CHAIN_SECURITY.md` - This documentation

### Modified:
- `back-end/build.gradle` - Added optional OWASP Dependency-Check plugin
- `front-end/package.json` - Added npm audit scripts

---

## Security Features Summary

| Feature | Type | Tool | When | Speed | Coverage |
|---------|------|------|------|-------|----------|
| Gradle Dependency Scan | Manual | OWASP Dependency-Check | On-demand | Slow (minutes) | All Java deps |
| npm Audit | Manual | npm audit | Anytime | Fast (seconds) | All Node deps |
| Auto Updates | Automated | Dependabot | Weekly | N/A | Gradle + npm + Actions |
| CI Integration | Automated | GitHub | Per push | Depends on config | Build + tests |

---

## Recommended Security Workflow

### Daily Development
```bash
# Build without dependency scan (fast)
./gradlew clean build

# Check frontend vulnerabilities (1-2 seconds)
npm audit
```

### Weekly (Every Monday)
- Monitor Dependabot PRs
- Review proposed updates
- Run tests
- Merge safe updates

### Monthly
```bash
# Deep security audit (optional)
cd back-end
./gradlew dependencyCheckAnalyze
# Review report: build/reports/dependency-check/dependency-check-report.html
```

---

## Expected Results

✅ **Vulnerable dependencies are detected** - Via npm audit and Dependabot  
✅ **Updates are controlled** - Via PR review process  
✅ **Build stays fast** - Dependency-check is optional, not required  
✅ **Supply chain is secured** - Automated monitoring with Dependabot  
✅ **No build slowdown** - npm audit is lightweight (1-2 seconds)  

---

## Quick Start Commands

### Start Now
```bash
# Test npm audit
cd front-end
npm audit

# Regular build (unchanged)
cd ../back-end
./gradlew clean build
```

### When You Have Time
```bash
# Deep security scan
cd back-end
./gradlew dependencyCheckAnalyze
```

### Monitor Updates
- Go to GitHub → Pull Requests
- Look for "dependabot[bot]" PRs (starting next Monday)
- Review and merge

---

## Troubleshooting

### npm audit shows vulnerabilities but no fix available
- Some require manual updates
- Check the advisory link provided: `npm audit`
- May need to wait for patched version

### Dependabot PRs not appearing
1. Ensure code is on GitHub
2. Wait for next Monday 3:00 AM UTC
3. Or manually check GitHub → Settings → Code security → Dependabot

### Want to run dependency-check on every build?
Edit `build.gradle` and uncomment:
```gradle
check.dependsOn dependencyCheckAnalyze
```
⚠️ Warning: Will slow down builds significantly on first run

---

## References

- [OWASP Dependency-Check](https://owasp.org/www-project-dependency-check/)
- [npm audit Documentation](https://docs.npmjs.com/cli/v8/commands/npm-audit)
- [GitHub Dependabot](https://docs.github.com/en/code-security/dependabot)
- [OWASP A03: Software Supply Chain Failures](https://owasp.org/Top10/A08_2021-Software_and_Data_Integrity_Failures/)
