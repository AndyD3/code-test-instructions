import './App.css'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { useState } from 'react'
import { ShortenUrlRequest } from './types'
import { useCreateUrl } from './hooks/useCreateUrl'
import { useDeleteUrl } from './hooks/useDeleteUrl'
import { Toaster } from 'react-hot-toast'
import { UrlsPaginated } from './UrlsPaginated'
import { UrlsStandard } from './UrlsStandard'

const queryClient = new QueryClient()
export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <URLManager />
    </QueryClientProvider>
  )
}

function URLManager() {

  const { mutate: createUrl } = useCreateUrl()
  const { mutate: deleteUrl } = useDeleteUrl()

  const [isChecked, setIsChecked] = useState(false)

  const checkHandler = () => {
    setIsChecked(!isChecked)
  }

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
      <Toaster
        position="top-center"
        reverseOrder={false}
        toastOptions={{
          duration: 5000,
          removeDelay: 1000
        }}        
      />
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

      <div>
          Paginated: 
          <input
          type="checkbox"
          id="checkbox"
          checked={isChecked}
          onChange={checkHandler}
          />
      </div>

      { 
        isChecked ? 
          <UrlsPaginated deleteUrl={deleteUrl}/>
        :
          <UrlsStandard deleteUrl={deleteUrl}/>          
      }
    </>
  )
}
