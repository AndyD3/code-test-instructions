export type ShortenedUrl = {
  alias: string, 
  fullUrl: string,
  shortUrl: string
}

export type ShortenUrlRequest = {
  fullUrl: string,
  customAlias?: string
}