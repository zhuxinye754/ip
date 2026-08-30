---
name: seedu-java-coding-standard
description: Apply the project's SE-EDU Java basic and intermediate coding standard when creating, editing, or reviewing Java code.
---

# SE-EDU Java coding standard

Apply the [SE-EDU Java coding standard (basic + intermediate)](https://se-education.org/guides/conventions/java/intermediate.html) to all production and test Java code in this repository. For topics that guide does not cover, use the Google Java Style Guide.

## Apply these rules

- Use English, meaningful names: PascalCase nouns for classes and enums, camelCase verbs for methods, camelCase variables, and `SCREAMING_SNAKE_CASE` constants. Boolean names should read as booleans (`is`, `has`, `can`, `should`, etc.); collection names are plural.
- Indent with four spaces, use K&R braces, keep lines at 120 characters or fewer (prefer 110 or fewer), and wrap continuations eight spaces beyond the parent indentation. Place spaces around binary operators and after commas.
- Use explicit imports only. Keep import groups in this project consistently ordered as static imports, `java`, third-party imports, then `clover` imports, with alphabetical order within a group.
- Put every type in a package. Keep declarations initialized and in the smallest useful scope. Do not expose mutable public fields.
- Always brace loop and conditional bodies, including single statements. Mark intentional fall-through in traditional `switch` statements with `// Fallthrough`.
- Write English Javadoc for every public production class and public method, except getters/setters and overrides whose inherited documentation applies unchanged. Use a concise third-person summary (for example, “Returns …” or “Adds …”) and include `@param`, `@return`, and `@throws` only when they add useful information.

## During implementation and review

Check changed Java files against these rules before testing. Preserve existing behavior unless the task specifically asks for a functional change. Treat test code as code for layout, naming, braces, whitespace, and import rules; test-method Javadoc is optional.
