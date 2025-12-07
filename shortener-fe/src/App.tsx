import './App.css'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { useReadUrls } from './hooks/useReadShortenedUrls'
import { useState } from 'react'
import { ShortenUrlRequest } from './types'
import { formToJSON } from 'axios'
import { useCreateUrl } from './hooks/useCreateUrl'

const queryClient = new QueryClient()

export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <URLManager />
    </QueryClientProvider>
  )
}

function URLManager() {
  const { data: shortenedUrls, isLoading, isError} = useReadUrls()

  const { mutate: createUrl, status: statusCreateUrl } = useCreateUrl()

  const emptyFormData = {
    fullUrl: '',
    customAlias: ''
  }

  const [formData, setFormData] = useState(emptyFormData)
  
  const handleChange = (e: { target: { name: string; value: string } }) => {
    const { name, value } = e.target
    setFormData((prevState) => ({ ...prevState, [name]: value }))
  }

  const handleSubmit = (event: React.MouseEvent<HTMLButtonElement>) => {
    event.preventDefault()

    const shortenUrlRequest: ShortenUrlRequest  = {
      fullUrl : formData.fullUrl,
      customAlias : formData.customAlias
    }

    createUrl(shortenUrlRequest);
    setFormData(emptyFormData)
  }

  return (
    <>
      <h1>URL Shortener tester</h1>
      

      <div className="label-wrap">
        <label>Full URL (required)</label>
        <input
          type="text"
          name="fullUrl"
          aria-label="fullUrl"
          value={formData.fullUrl}
          onChange={handleChange}
        />
      </div>
      <div className="label-wrap">
        <label>Custom alias (optional)</label>
        <input
          type="text"
          name="customAlias"
          aria-label="customAlias"
          value={formData.customAlias}
          onChange={handleChange}
        />
      </div>

      <div className="buttonPanel">
        <button
          disabled={formData.fullUrl == ''}
          className="cta"
          aria-label="create"
          onClick={handleSubmit}
        >
          Create Short URL
        </button>
      </div>

      {isError && <div style={{color: "red"}}>An error has occurred...</div>}

      {isLoading && <div>Loading...</div>}    

      <h2>Stored URLs</h2>
      <table className="dataTable">
        <thead>
          <td>Short URL</td>
          <td>Full URL</td>
        </thead>
        <tbody>
          {shortenedUrls?.length == 0 ? (
              <tr>
                <td colSpan={3}>No Urls</td>
              </tr>
            ) : (
              shortenedUrls?.map((url) => ( 
                <tr key={url.shortUrl}>
                  <td><a href={url.shortUrl}>{url.shortUrl}</a></td>
                  <td>{url.fullUrl}</td>
                </tr>
                )
              )
            )
          }
        </tbody>
      </table>
    </>
  )
}
