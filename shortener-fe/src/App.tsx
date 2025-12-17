import './App.css'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { useReadUrls } from './hooks/useReadShortenedUrls'
import { useState } from 'react'
import { ShortenUrlRequest } from './types'
import { formToJSON } from 'axios'
import { useCreateUrl } from './hooks/useCreateUrl'
import { useDeleteUrl } from './hooks/useDeleteUrl'

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

  const { mutate: createUrl, status: statusCreateUrl, error: isErrorCreateURL, isSuccess} = useCreateUrl()
  const { mutate: deleteUrl } = useDeleteUrl()

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

      
      {isSuccess && <div style={{color: "green"}}>Created successfully</div>}

      {isErrorCreateURL && <div style={{color: "red"}}>An error has occurred creating short url</div>}

      {isError && <div style={{color: "red"}}>An error has occurred reading URLs...</div>}

      {isLoading && <div>Loading...</div>}    

      <h2>Stored URLs</h2>
      <table className="dataTable">
        <thead>
          <tr>
            <th>Alias</th>
            <th>Short URL</th>
            <th>Full URL</th>
            <th/>
          </tr>
        </thead>
        <tbody>
          {shortenedUrls?.length == 0 ? (
              <tr>
                <td colSpan={3}>No Urls</td>
              </tr>
            ) : (
              shortenedUrls?.map((url) => ( 
                <tr key={url.alias}>
                  <td>{url.alias}</td>
                  <td><a href={url.shortUrl} target="_blank">{url.shortUrl}</a></td>
                  <td>{url.fullUrl}</td>
                  <td>
                    <button
                      aria-label="delete"
                      className="caution"
                      onClick={() => deleteUrl(url.shortUrl!)}
                    >
                    Delete
                    </button>
                  </td>
                </tr>
                )
              )  
            )
          }
           <tr>
              <td colSpan={3}></td>
              <td>
                <button
                  aria-label="delete"
                  className="caution"
                  onClick={() => deleteUrl("non_existant_url")}
                >
                Delete (test non existant alias)
                </button>
              </td>
           </tr>

        </tbody>
      </table>
    </>
  )
}
