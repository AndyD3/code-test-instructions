import { UseBaseMutationResult } from '@tanstack/react-query'

import { useMutation, useQueryClient } from '@tanstack/react-query'
import { ShortenUrlRequest } from '../types'
import { AxiosError, AxiosResponse } from 'axios'
import axiosClient from 'axios'
import toast from 'react-hot-toast'

const createUrl = async (shortUrlRequest: ShortenUrlRequest): Promise<AxiosResponse<ShortenUrlRequest>> => {
  return await axiosClient.post<ShortenUrlRequest>('http://localhost:8080/shorten', shortUrlRequest)
}

export const useCreateUrl = (): UseBaseMutationResult<
  AxiosResponse<ShortenUrlRequest>,
  unknown,
  ShortenUrlRequest,
  unknown
> => {
  const queryClient = useQueryClient()

  return useMutation({
    mutationFn: (shortUrlRequest : ShortenUrlRequest ) => createUrl(shortUrlRequest),
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ['shortenedUrls'] })
      toast.success(`Short URL successfully created`)
    }
    ,  
    onError: (error: AxiosError) => toast.error(`Error creating Short URL. Error message was:\n ${error?.response?.data ? error?.response?.data : "<no message body>"}`)    
  })
}
